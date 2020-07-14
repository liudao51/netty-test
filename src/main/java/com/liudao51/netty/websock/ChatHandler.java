package com.liudao51.netty.websock;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.Date;

/**
 * 处理消息的handler
 * TextWebSocketFrame: 在netty中，是用于为websocket专门处理文本的对象，frame是消息的载体
 */
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    //用于记录和管理所有客户端的channel
    private static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        //获取客户端传过来的内容
        String content = msg.text();
        System.out.println("接收到的内容：" + content);

//        for (Channel channel : clients) {
//            channel.writeAndFlush(new TextWebSocketFrame("[服务器接收到消息：]" + new Date().toString() + "接受到消息为：" + content));
//        }

        //下面的方法，和上面的for循环功能是一样的
        clients.writeAndFlush(new TextWebSocketFrame("[服务器接收到消息：]" + new Date().toString() + "接受到消息为：" + content));
    }

    /**
     * 当客户端连接服务端之后（打开链接）
     * 获取客户端的channel，并放到ChannelGroup中进行管理
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端连接服务端channel:" + ctx.channel().id().asLongText());
        //super.channelRegistered(ctx);
        clients.add(ctx.channel());
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("移除对应的客户端channel:" + ctx.channel().id().asLongText());

        //super.channelUnregistered(ctx);
        //当触发channelUnregistered, ChannelGroup会自动移除对应的客户端的channel,所以下面代码clients.remove()是多余的
        //clients.remove(ctx.channel());
    }
}
