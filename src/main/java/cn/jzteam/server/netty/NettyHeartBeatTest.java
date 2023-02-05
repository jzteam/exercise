package cn.jzteam.server.netty;

import cn.jzteam.utils.SigarUtil;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class NettyHeartBeatTest {

    public static void main(String[] args) {
        NettyUtil.openServer(8888,new ServerHeartBeatHandler());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        NettyUtil.openClient("127.0.0.1",8888,(ctx)->{
            System.out.println("用户什么都没干");
        },new ClientHeartBeatHandler());
    }

}

/**
 * 服务器端的心跳处理
 */
class ServerHeartBeatHandler extends ChannelInboundHandlerAdapter {
    private static final Map<String,String> AUTH_IP_MAP = new HashMap<>();
    private static final String SUCCESS_KEY ="auth_success_key";

    static {
        AUTH_IP_MAP.put("192.168.150.101","1234");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if(msg instanceof String){
                // 第一次请求鉴权
                String[] rets = ((String)msg).split(",");
                String auth = AUTH_IP_MAP.get(rets[0]);
                if(auth != null &&auth.equals(rets[1])) {
                    ctx.writeAndFlush(SUCCESS_KEY);
                } else {
                    ctx.writeAndFlush("auth failure!").addListener(ChannelFutureListener.CLOSE);
                }
            }else if(msg instanceof RequestInfo){
                // 心跳信息
                RequestInfo info = (RequestInfo)msg;

                System.out.println("----------------------------------------------");
                System.out.println("当前主机ip：" +info.getIp());
                System.out.println("当前主机cpu：情况");

                Map<String, Object> cpuMap =info.getCpuPercMap();
                System.out.println("总使用率：" +  cpuMap.get("combined"));
                System.out.println("用户使用率：" +cpuMap.get("user"));
                System.out.println("系统使用率：" +cpuMap.get("sys"));
                System.out.println("等待率：" +cpuMap.get("wait"));
                System.out.println("空闲率：" +cpuMap.get("idle"));
                System.out.println("当前主机memory情况：");

                Map<String, Object> memMap =info.getMemoryMap();
                System.out.println("内存总量：" +memMap.get("total"));
                System.out.println("当前内存使用量：" +memMap.get("used"));
                System.out.println("当前内存剩余量：" +memMap.get("free"));
                System.out.println("-----------------------------------------------");

                ctx.writeAndFlush("info received!");
            } else {
                // 其他信息或者null，返回连接失败
                ctx.writeAndFlush("connect failure").addListener(ChannelFutureListener.CLOSE);
            }
        }catch (Exception e){
            System.out.println("ServerHeartBeatHandler error ...");
        }
    }

    private boolean auth(ChannelHandlerContext ctx, Object msg) {
        String[] rets = ((String)msg).split(",");
        String auth = AUTH_IP_MAP.get(rets[0]);
        if(auth != null &&auth.equals(rets[1])) {
            ctx.writeAndFlush(SUCCESS_KEY);
            return true;
        } else {
            ctx.writeAndFlush("authfailure!").addListener(ChannelFutureListener.CLOSE);
            return false;
        }
    }
}

/**
 * 客户端的心跳发送
 */
class ClientHeartBeatHandler extends ChannelInboundHandlerAdapter{
    private ScheduledExecutorService scheduled= Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> heartBeat;
    private InetAddress address;
    private static final String SUCCESS_KEY ="auth_success_key";

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        // 发生异常，心跳信息不再发送
        if(heartBeat != null) {
            heartBeat.cancel(true);
            heartBeat = null;
        }
        ctx.fireExceptionCaught(cause);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        // 初始化address
        address = InetAddress.getLocalHost();
        String ip = address.getHostAddress();
        String key = "1234";
        String auth = ip + "," + key;
        // 通道连接，输入鉴权信息：ip+key
        ctx.writeAndFlush(auth);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if(msg instanceof String) {
                String data = (String) msg;
                if(SUCCESS_KEY.equals(data)) {
                    // 服务端返回auth成功，那么就启动心跳检测线程，向服务器发送本机状态信息
                    heartBeat =scheduled.scheduleWithFixedDelay(new HeartBeatTask(ctx, address), 0, 4, TimeUnit.SECONDS);
                    System.out.println(msg);
                } else {
                    // 其他信息就是简单打印
                    System.out.println(msg);
                }
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }
}

/**
 * 心跳task，sigar从本机获取数据，组装成对象写入ctx
 */
class HeartBeatTask implements Runnable{
    private final ChannelHandlerContext ctx;
    private InetAddress address;
    public HeartBeatTask(ChannelHandlerContext ctx, InetAddress address) {
        this.ctx = ctx;
        this.address = address;
    }

    @Override
    public void run() {
        try {
            RequestInfo requestInfo = new RequestInfo();
            requestInfo.setIp(address.getHostAddress());
            CpuPerc cpuPerc =SigarUtil.sigar.getCpuPerc();
            Map<String, Object>cpuPercMap = new HashMap<>();
            cpuPercMap.put("combined",cpuPerc.getCombined());
            cpuPercMap.put("user", cpuPerc.getUser());
            cpuPercMap.put("sys",cpuPerc.getSys());
            cpuPercMap.put("wait", cpuPerc.getWait());
            cpuPercMap.put("idle",cpuPerc.getIdle());

            Mem mem = SigarUtil.sigar.getMem();
            Map<String, Object> memoryMap = new HashMap<>();
            memoryMap.put("total", mem.getTotal() / (1024 * 1024));
            memoryMap.put("used",mem.getUsed() / (1024 * 1024));
            memoryMap.put("free",mem.getFree() / (1024 * 1024));

            requestInfo.setCpuPercMap(cpuPercMap);
            requestInfo.setMemoryMap(memoryMap);

            ctx.writeAndFlush(requestInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

class RequestInfo implements Serializable {
    private String ip;
    private Map<String,Object> cpuPercMap;
    private Map<String,Object> memoryMap;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Map<String, Object> getCpuPercMap() {
        return cpuPercMap;
    }

    public void setCpuPercMap(Map<String, Object> cpuPercMap) {
        this.cpuPercMap = cpuPercMap;
    }

    public Map<String, Object> getMemoryMap() {
        return memoryMap;
    }

    public void setMemoryMap(Map<String, Object> memoryMap) {
        this.memoryMap = memoryMap;
    }
}