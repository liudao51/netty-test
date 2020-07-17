package com.liudao51.netty.groupchat;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * 自定义客户端处理器handler
 * 通道中消息类型建议使用netty提供的高速缓冲区ByteBuf类型(不建议使用String类型)
 */
public class GroupchatClientHandler extends SimpleChannelInboundHandler<Object> {
    /**
     * channelRead0方法：读取数据
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel channel = ctx.channel();
        ByteBuf buf = (ByteBuf) msg;

        //这里不用通知服务端，所以直接System.out.println即可
        System.out.println("服务端[" + channel.remoteAddress() + "]发送的消息是：" + buf.toString(CharsetUtil.UTF_8));
    }
}
