package cn.jzteam.core.socket;

import cn.jzteam.utils.GzipUtil;
import cn.jzteam.utils.MarshallingUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.ReferenceCountUtil;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Serializable;

public class NettyTest {

    public static void main(String[] args) {
        final NettyTest instance = new NettyTest();
        new Thread(()->{
            instance.openServer(8888);
        }).start();

        new Thread(()->{
            try {
                instance.openImageClient();
            }catch (Exception e){
                System.out.println("怎么了imageClient");
            }
        }).start();

        System.out.println("主线程结束");
    }

    public void openServer(int port){
        EventLoopGroup bossGroup = new NioEventLoopGroup(); //用于处理服务器端接收客户端连接
        EventLoopGroup workerGroup = new NioEventLoopGroup(); //进行网络通信（读写）
        try {
            ServerBootstrap bootstrap = new ServerBootstrap(); //辅助工具类，用于服务器通道的一系列配置
            bootstrap.group(bossGroup, workerGroup) //绑定两个线程组
                    .channel(NioServerSocketChannel.class) //指定NIO的模式
                    .childHandler(new ChannelInitializer<SocketChannel>() { //配置具体的数据处理方式
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
//                            socketChannel.pipeline().addLast(new StringDecoder());// 将数据由buf转换成String
//                            socketChannel.pipeline().addLast(new ServerHandler());// 自定义Handler放在最后
                            socketChannel.pipeline().addLast(MarshallingUtil.buildMarshallingDecoder());// 对象解码器
                            socketChannel.pipeline().addLast(MarshallingUtil.buildMarshallingEncoder());// 对象编码器
                            socketChannel.pipeline().addLast(new ServerClassHandler());// 自定义Handler放在最后
                        }
                    })
                    /**
                     * 对于ChannelOption.SO_BACKLOG的解释：
                     * 服务器端TCP内核维护有两个队列，我们称之为A、B队列。客户端向服务器端connect时，会发送带有SYN标志的包（第一次握手），服务器端
                     * 接收到客户端发送的SYN时，向客户端发送SYN ACK确认（第二次握手），此时TCP内核模块把客户端连接加入到A队列中，然后服务器接收到
                     * 客户端发送的ACK时（第三次握手），TCP内核模块把客户端连接从A队列移动到B队列，连接完成，应用程序的accept会返回。也就是说accept
                     * 从B队列中取出完成了三次握手的连接。
                     * A队列和B队列的长度之和就是backlog。当A、B队列的长度之和大于ChannelOption.SO_BACKLOG时，新的连接将会被TCP内核拒绝。
                     * 所以，如果backlog过小，可能会出现accept速度跟不上，A、B队列满了，导致新的客户端无法连接。要注意的是，backlog对程序支持的
                     * 连接数并无影响，backlog影响的只是还没有被accept取出的连接
                     */
                    .option(ChannelOption.SO_BACKLOG, 1024) //设置TCP缓冲区
                    .option(ChannelOption.SO_SNDBUF, 3 * 1024 * 1024) //设置发送数据缓冲大小
                    .option(ChannelOption.SO_RCVBUF, 3 * 1024 * 1024) //设置接受数据缓冲大小
                    .option(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true); //保持连接
            ChannelFuture future = bootstrap.bind(port).sync();
            System.out.println("服务器已开启 "+port);
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public void openClient() throws InterruptedException {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workerGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new StringDecoder());// 将数据由buf转换成String
                        socketChannel.pipeline().addLast(new ClientHandler());
                    }
                });
        ChannelFuture future = bootstrap.connect("127.0.0.1", 8888).sync();
        future.channel().writeAndFlush(Unpooled.copiedBuffer("777".getBytes()));
        future.channel().closeFuture().sync();
        workerGroup.shutdownGracefully();
    }

    public void openImageClient() throws InterruptedException {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workerGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(MarshallingUtil.buildMarshallingEncoder());// 对象编码器
                        socketChannel.pipeline().addLast(MarshallingUtil.buildMarshallingDecoder());// 对象解码器
                        socketChannel.pipeline().addLast(new ReadTimeoutHandler(5)); //5秒后未与服务器通信，则断开连接。
                        socketChannel.pipeline().addLast(new ClientClassHandler());
                    }
                });
        ChannelFuture future = bootstrap.connect("127.0.0.1", 8888).sync();

        System.out.println("client connect success");
        try {
            // 组装Request
            for (int i = 1; i <= 5; i++) {
                Request request = new Request();
                request.setId(i + "");
                request.setMessage("上传第" + i + "张图片");
                //传输图片
                FileInputStream inputStream = new FileInputStream("/Users/oker/work/test/"+i+".jpg");
                byte[] data = new byte[inputStream.available()];
                inputStream.read(data);
                inputStream.close();
                byte[] gzipData = GzipUtil.gzip(data);
                request.setImg(gzipData);
                future.channel().writeAndFlush(request);

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    System.out.println("sleep over");
                }
            }

            future.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            workerGroup.shutdownGracefully();
        }
    }

}

class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        String request = "";

//        ByteBuf buf = (ByteBuf)msg;
//        request = buf.toString(Charset.forName("UTF-8"));

        // 因为使用了StringDecoder，msg就是String了
        request = (String)msg;
        System.out.println("Server收到: " + request);
        //写给客户端
        String response = "我是反馈的信息";
        ctx.writeAndFlush(Unpooled.copiedBuffer("888".getBytes()));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}

class ServerClassHandler extends ChannelInboundHandlerAdapter {

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

            // 获取Request
            Request request = (Request) msg;
            System.out.println("Server:" + request.getId() + "," + request.getMessage());

            // 封装Response
            Response response = new Response();
            response.setCode(Integer.valueOf(request.getId()));
            response.setMsg("响应内容： " + request.getMessage());

            if(request.getImg() != null && request.getImg().length > 0){
                // 解压图片
                byte[] unGizpData = GzipUtil.unGzip(request.getImg());
                // 保存图片到磁盘
                FileOutputStream outputStream = new FileOutputStream("/Users/oker/work/test/receive/" + request.getMessage() + ".jpg");
                outputStream.write(unGizpData);
                outputStream.flush();
                outputStream.close();
            }

            // 向通道写入Response
            ctx.writeAndFlush(response);
        }catch (Exception e){
            System.out.println("ServerClassHandler error ...");
        }
    }
}

class ClientClassHandler extends ChannelInboundHandlerAdapter {

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

        Response response = (Response) msg;
        System.out.println("client获取响应：code="+response.getCode()+",message="+response.getMsg());
    }

}

class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            // 因为使用了StringDecoder，msg就是String了
            String response = (String)msg;
            System.out.println("client收到回复：" + response);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}

class Request implements Serializable {

    private String id;
    private String message;
    private byte[] img;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }
}

class Response implements Serializable {
    static final long serialVersionUID = 1L;
    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}