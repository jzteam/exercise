package cn.jzteam.module.netty.chapter2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class EchoServer {
    
    private int port;
    
    public EchoServer(int port){
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException {
        new EchoServer(8889).start();
    }
    
    public void start() throws InterruptedException {
        // 自定义信息处理器
        final EchoServerHandler echoServerHandler = new EchoServerHandler();
        // 信息处理线程池
        final NioEventLoopGroup eventExecutors = new NioEventLoopGroup();
        
        // 启动器
        final ServerBootstrap serverBootstrap = new ServerBootstrap();
        try {
            serverBootstrap.group(eventExecutors) // 指定线程池
                    .channel(NioServerSocketChannel.class) // 指定通信类型，同步或异步
                    .localAddress(new InetSocketAddress(port)) // 指定服务地址端口
                    .childHandler(new ChannelInitializer<SocketChannel>() { // 将自定义信息处理添加到启动器里面
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(echoServerHandler);
                        }
                    });
            // 异步绑定服务器，通过sync阻塞直到绑定完成
            final ChannelFuture future = serverBootstrap.bind().sync();
            // 异步获取该连接的CloseFuture，通过sync阻塞直到该线程完成
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 关闭线程池，释放资源
            eventExecutors.shutdownGracefully().sync();
        }

    }
}
