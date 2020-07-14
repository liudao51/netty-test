package com.liudao51.netty.websock;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * netty websock测试
 */
public class WebSocketServerApplication {
    public static void main(String[] args) {

        //定义主线程组(只用于接收客户端链接,并把任务分配给从线程组处理,自己不处理具体的任务)
        EventLoopGroup bossGroup = new NioEventLoopGroup();

        //定义从线程组(处理具体的任务)
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {

            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup) //绑定主线程组与从线程组
                    .channel(NioServerSocketChannel.class) //创建双向通道
                    .childHandler(new WsServerIntializer()); //指定Pipeline的子处理器

            //启动server,并设置8088为启动端口,启动方式为同步
            ChannelFuture channelFuture = serverBootstrap.bind(8088).sync();

            //监听关闭的channel,关闭方式为同步
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
