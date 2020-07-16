package com.liudao51.netty.echo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * 服务端处理器handler
 */
//由于我们的应用很简单，只需要继承 ChannelInboundHandlerAdapter 就行了。这个类 提供了默认 ChannelInboundHandler 的实现。另外直接继承SimpleChannelInboundHandler类也是可以的。
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 每个信息入站都会调用
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("客户端[" + ctx.channel().remoteAddress() + "],发送的消息是：" + buf.toString(CharsetUtil.UTF_8));
    }

    /**
     * 当前批处理中的最后一条消息时调用
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("Hello,客户端~", CharsetUtil.UTF_8));
    }

    /**
     * 读操作时捕获到异常时调用
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();

        // 关闭通道
        //在这种情况下，我们记录并关闭所有可能处于未知状态的连接。它通常是难以 从连接错误中恢复，所以干脆关闭远程连接。
        // 当然，也有可能的情况是可以从错误中恢复的，所以可以用一个更复杂的措施来尝试识别和处理 这样的情况。
        ctx.close();
    }
}
