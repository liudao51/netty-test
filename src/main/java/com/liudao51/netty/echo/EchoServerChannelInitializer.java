package com.liudao51.netty.echo;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;


/**
 * ChannelInitializer是一个特殊的ChannelInboundHandler，是用于在某个Channel注册到EventLoop后，对这个Channel进行初始化（如：配置管道pipeline中想要添加的handler）。
 * ChannelInitializer虽然会在一开始会被注册到Channel相关的pipeline里，但是在初始化完成之后，ChannelInitializer会将自己从pipeline中移除，不会影响后续的操作。
 */
public class EchoServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        //pipeline.addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
        //pipeline.addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
        pipeline.addLast(new EchoServerHandler()); //添加自定义handler
    }
}
