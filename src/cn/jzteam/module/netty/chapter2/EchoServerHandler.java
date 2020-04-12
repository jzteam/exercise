package cn.jzteam.module.netty.chapter2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // netty 读到数据时调用
        ByteBuf in = (ByteBuf) msg;
        // 打印收到的信息
        final String content = in.toString(CharsetUtil.UTF_8);
        System.out.println("Server received: " + content);
        // 把收到的信息写给发送者，但不立即冲刷缓存
        final String test = content.substring(0, content.length() - 2);
        System.out.println("回复："+test);
        final ByteBuf result = ByteBufUtil.writeUtf8(ByteBufAllocator.DEFAULT, test);
        ctx.write(result);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // netty 读完数据时调用
        // 冲刷缓存（将缓存中的信息写到socket中了）
        // 添加一个关闭监听器，等待客户端的关闭fin报文段
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 出现异常时调用
        // 打印异常栈
        cause.printStackTrace();
        // 关闭tcp连接
        ctx.close();
    }
}
