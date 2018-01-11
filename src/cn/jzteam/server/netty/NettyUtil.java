package cn.jzteam.server.netty;

import cn.jzteam.utils.MarshallingUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.util.function.Consumer;

public class NettyUtil {

    public static void openServer(int port, ChannelHandler... handlers){
        new Thread(() -> {
            EventLoopGroup bossGroup = new NioEventLoopGroup(); //用于处理服务器端接收客户端连接
            EventLoopGroup workerGroup = new NioEventLoopGroup(); //进行网络通信（读写）
            try {
                ServerBootstrap bootstrap = new ServerBootstrap(); //辅助工具类，用于服务器通道的一系列配置
                bootstrap.group(bossGroup, workerGroup) //绑定两个线程组
                        .channel(NioServerSocketChannel.class) //指定NIO的模式
                        .childHandler(new ChannelInitializer<SocketChannel>() { //配置具体的数据处理方式
                            @Override
                            protected void initChannel(SocketChannel socketChannel) throws Exception {
                                socketChannel.pipeline().addLast(MarshallingUtil.buildMarshallingDecoder());// 对象解码器
                                socketChannel.pipeline().addLast(MarshallingUtil.buildMarshallingEncoder());// 对象编码器
                                // 自定义Handler放在最后
                                for(int i = 0;i<handlers.length;i++){
                                    socketChannel.pipeline().addLast(handlers[i]);
                                }
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
        }).start();

    }

    public static void openClient(String host, int port, Consumer<ChannelFuture> consumer, ChannelHandler...handlers) {
        new Thread(()->{
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            Bootstrap bootstrap = new Bootstrap();
            try{
                bootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(MarshallingUtil.buildMarshallingEncoder());// 对象编码器
                            socketChannel.pipeline().addLast(MarshallingUtil.buildMarshallingDecoder());// 对象解码器
                            socketChannel.pipeline().addLast(new ReadTimeoutHandler(5)); //5秒后未与服务器通信，则断开连接。
                            // 自定义Handler放在最后
                            for(int i = 0;i<handlers.length;i++){
                                socketChannel.pipeline().addLast(handlers[i]);
                            }
                        }
                    });
                ChannelFuture future = bootstrap.connect(host, port).sync();
                System.out.println("client connect success");

                // 执行client输入操作
                consumer.accept(future);

                future.channel().closeFuture().sync();
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                workerGroup.shutdownGracefully();
            }
        }).start();

    }
}
