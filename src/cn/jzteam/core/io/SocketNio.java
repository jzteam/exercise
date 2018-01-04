package cn.jzteam.core.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

public class SocketNio {

    public static void main(String[] args) {
        final SocketNio instance = new SocketNio();
        new Thread(()->{
            try {
                instance.serverTest();
            }catch (Exception e){}
        }).start();

        new Thread(()->{
            try{
                instance.clientTest();
            }catch (Exception e){}
        }).start();

    }

public void serverTest() throws IOException{
    // 一个线程，持有的所有客户端连接socket
    Set<SocketChannel> channelSet = new HashSet<>();
    // 创建一个选择器，用于持有所有注册进来的channel，同时可以轮询这些channel检查其状态
    Selector selector = Selector.open();

    // 获取server通道
    ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
    // 设置non-blocking
    serverSocketChannel.configureBlocking(false);
    // 绑定ip和port
    serverSocketChannel.bind(new InetSocketAddress("127.0.0.1", 3044));
    // 首先把服务器accept通道注册到selector，监听OP_ACCEPT事件
    serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

    // 轮询selector中所有的channel
    while(selector.select() > 0){
        // 获取可操作的事件：只有read和accept事件。
        // 因为write事件是自己线程执行写入，一直忙碌中，不需要监听，
        // 只有read和accept是等待其他线程，所以这两个才需要监听
        Set<SelectionKey> selectedKeys = selector.selectedKeys();
        Iterator<SelectionKey> iterator = selectedKeys.iterator();
        if(iterator.hasNext()){
            SelectionKey key = iterator.next();
            // accept事件
            if(key.isAcceptable()){
                // 接受连接，获得一个与客户端对应的channel
                SocketChannel accept = serverSocketChannel.accept();
                // 设置为non-blocking
                accept.configureBlocking(false);
                // 客户端的消息发送过来很慢，需要等待，将其注册到selector中，监听read事件
                accept.register(selector, SelectionKey.OP_READ);
                // 这里仅仅是额外持有channel。其实selector已经持有所有channel了。
                channelSet.add(accept);
            }else if(key.isReadable()){
                // 从key中获取当前事件的channel
                SocketChannel channel = (SocketChannel)key.channel();
                // 读取使用的缓存
                ByteBuffer buf = ByteBuffer.allocate(1024);
                // 回复客户端，写入使用的缓存
                ByteBuffer back = ByteBuffer.allocate(1024);
                while(channel.read(buf) > 0){
                    buf.flip();
                    back.put(buf);
                    buf.clear();
                }
                back.put("\n".getBytes());
                // 将limit设置为当前position，表示buffer准备好被用来读取了
                back.flip();
                // 将buffer中0->position字节写入channel
                channel.write(back);
                back.clear();
            }else{
                System.out.println("啥都不是");
            }
            // 需要将key移除。听说不移除会导致空轮询
            iterator.remove();
        }
    }
    serverSocketChannel.close();
}
    
public void clientTest() throws IOException{
    // 连接服务器，获取一个channel
    SocketChannel channel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 3044));
    // 设置为non-blocking
    channel.configureBlocking(false);

    // 立即开启一个线程，用于读取消息
    new Thread(()->{
        try {
            new SocketNio().clientReadTest(channel);
        }catch (Exception e){}
    }).start();

    // 当前线程用户监听键盘，时刻输入消息
    Scanner scan = new Scanner(System.in);
    ByteBuffer buf = ByteBuffer.allocate(1024);
    while(scan.hasNextLine()){
        String line = scan.nextLine();
        buf.put((new Date().toString()+":"+line).getBytes());
        buf.flip();
        channel.write(buf);
        buf.clear();
    }
    scan.close();
    channel.close();
}

public void clientReadTest(SocketChannel channel) throws IOException {
    // 创建一个选择器
    Selector selector = Selector.open();
    // 把传入的通道注册进来，监听read
    channel.register(selector, SelectionKey.OP_READ);

    while(selector.select() > 0){
        Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
        if(iterator.hasNext()){
            SelectionKey key = iterator.next();
            if(key.isReadable()){
                ByteBuffer buf = ByteBuffer.allocate(1024);
                SocketChannel sc = (SocketChannel)key.channel();
                int len;
                while((len = sc.read(buf)) > 0){
                    buf.flip();
                    System.out.println("server回复消息："+ new String(buf.array(),0,len));
                    buf.clear();
                }
            }else{
                System.out.println("啥都不是");
            }
            iterator.remove();
        }
    }
}
    
}
