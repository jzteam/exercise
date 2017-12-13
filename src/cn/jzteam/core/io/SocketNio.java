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

    public void serverTest() throws IOException{
        
        Set<SocketChannel> channelSet = new HashSet<>();
        
        // 获取通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress("127.0.0.1", 3044));
        
        // 获取选择器
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        
        while(selector.select() > 0){
//            System.out.print("selector发现有动静：");
            
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectedKeys.iterator();
            if(iterator.hasNext()){
                SelectionKey key = iterator.next();
                
                if(key.isAcceptable()){
//                    System.out.println("是accept准备好了");
                    SocketChannel accept = serverSocketChannel.accept();
                    accept.configureBlocking(false);
                    accept.register(selector, SelectionKey.OP_READ);
                    channelSet.add(accept);
                }else if(key.isReadable()){
//                    System.out.println("是read准备好了");
                    ByteBuffer buf = ByteBuffer.allocate(1024);
                    SocketChannel channel = (SocketChannel)key.channel();
                    int len = 0;
                    while((len = channel.read(buf)) > 0){
                        buf.flip();
                        System.out.println("buf.position="+buf.position()+",len="+len);
                        System.out.println(new String(buf.array(),0,len));
                        buf.clear();
                    }
                }else{
                    System.out.println("啥都不是");
                }
                iterator.remove();
            }
        }
        
        
        serverSocketChannel.close();
        
    }
    
    public void clientTest() throws IOException{
        
        SocketChannel channel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 3044));
        channel.configureBlocking(false);
        
        Scanner scan = new Scanner(System.in);
        ByteBuffer buf = ByteBuffer.allocate(1024);
        while(scan.hasNextLine()){
            String line = scan.nextLine();
            buf.put((new Date().toString()+"\n"+line).getBytes());
            buf.flip();
            channel.write(buf);
            buf.clear();
        }
        scan.close();
        channel.close();
    }
    
}
