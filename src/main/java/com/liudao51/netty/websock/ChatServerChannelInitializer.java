package com.liudao51.netty.websock;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;


/**
 * 初始化器，channel注册后，会执行里面的相应的初始化方法
 */
public class ChatServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        //通过SocketChannel去获得对应的pipeline管道
        ChannelPipeline pipeline = channel.pipeline();

        //============== 以下是用于支持Http协议 ===============

        //通过管道添加handler(拦截器)
        //HttpServerCodec助手类，用于编码与解码
        pipeline.addLast(new HttpServerCodec());

        //对写大数据流的支持
        pipeline.addLast(new ChunkedWriteHandler());

        //对httpMessage进行聚合,聚合成FullHttpRequest或FullHttpRespone
        //几乎在netty中的编程,都会使用到此handler
        pipeline.addLast(new HttpObjectAggregator(1024 * 64));


        //============== 以下是用于支持httpWebscoket协议 ===============

        /**
         * websocket 服务器处理的协议，用于指定给客户端连接访问路由：/ws
         * 本handler会帮你处理一些繁重的复杂的事：如握手动作handsshaking(close,ping,pong) ping+pong=心跳
         * 对于websock来讲，都是以frames进行传输的，不同的数据类型对应的frames也不同
         */
        pipeline.addLast(new WebSocketServerProtocolHandler("/chat"));

        //添加自定义助手类
        pipeline.addLast(new ChatHandler());
    }
}
