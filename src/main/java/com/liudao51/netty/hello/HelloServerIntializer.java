package com.liudao51.netty.hello;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * 初始化器，channel注册后，会执行里面的相应的初始化方法
 */
public class HelloServerIntializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        //通过SocketChannel去获得对应的pipeline管道
        ChannelPipeline pipeline = socketChannel.pipeline();

        //通过管道添加handler(拦截器)
        //HttpServerCodec助手类，用于编码与解码
        pipeline.addLast(new HttpServerCodec());

        //添加自定义助手类
        pipeline.addLast(new HelloHandler());
    }
}
