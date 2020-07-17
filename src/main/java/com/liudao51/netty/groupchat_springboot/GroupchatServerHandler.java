package com.liudao51.netty.groupchat_springboot;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * 自定义处理器handler
 */
public class GroupchatServerHandler extends SimpleChannelInboundHandler<Object> {

    //定义channel组，用于管理所有的channel，这里指定为static以用于全部Handler共享该channelGroup对象
    //GlobalEventExecutor.INSTANCE 是全局的事件执行器，是一个单例
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /*
     * handlerAdded方法：连接一旦建立，第一个被执行的方法（只会执行一次）
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();

        //通知channelGroup中的所有channel(除自己外，所以这里需要在channelGroup.add方法前通知)
        channelGroup.writeAndFlush(Unpooled.copiedBuffer("客户端[" + channel.remoteAddress() + "]加入聊天室...", CharsetUtil.UTF_8));

        //把自己加入channelGroup中
        channelGroup.add(channel);
    }

    /**
     * handlerRemoved方法：连接断开，最后一个被执行的方法（只会执行一次）
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush(Unpooled.copiedBuffer("客户端[" + channel.remoteAddress() + "]退出聊天室...", CharsetUtil.UTF_8));

        //channelGroup.remove这句话是多余的，因为channelGroup会自动移除当前channel
        //channelGroup.remove(channel);
        System.out.println("channelGroup size is " + channelGroup.size());
    }

    /**
     * channelActive方法：channel处理活动状态
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //这里不用通知客户端，所以直接System.out.println即可
        System.out.println("客户端[" + channel.remoteAddress() + "]上线了...");
    }

    /**
     * channelInactive方法：channel处理不活动状态
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //这里不用通知客户端，所以直接System.out.println即可
        System.out.println("客户端[" + channel.remoteAddress() + "]下线了...");
    }

    /**
     * channelRead0方法：读取数据
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel channel = ctx.channel();
        ByteBuf buf = (ByteBuf) msg;

        for (Channel ch : channelGroup) {
            if (channel != ch) {
                ch.writeAndFlush(Unpooled.copiedBuffer("客户端[" + channel.remoteAddress() + "]发送了消息：" + buf.toString(CharsetUtil.UTF_8), CharsetUtil.UTF_8));
            } else {
                ch.writeAndFlush(Unpooled.copiedBuffer("自己[" + channel.remoteAddress() + "]发送了消息：" + buf.toString(CharsetUtil.UTF_8), CharsetUtil.UTF_8));
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
