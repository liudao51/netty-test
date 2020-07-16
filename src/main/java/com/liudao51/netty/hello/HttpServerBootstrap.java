package com.liudao51.netty.hello;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * netty http 测试
 */
public class HttpServerBootstrap {

    private final String host;
    private final Integer port;

    public HttpServerBootstrap(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    /**
     * netty服务器启动方法
     */
    public void start() {

        //boss线程组(只用于接收客户端链接,并把任务分配给从线程组处理,自己不处理具体的任务)
        EventLoopGroup bossGroup = new NioEventLoopGroup();

        //worker线程组(处理具体的任务)
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup) //绑定主线程组与从线程组
                    .channel(NioServerSocketChannel.class) //使用NioServerSocketChannel作为服务器的通道实现
                    .childHandler(new HelloServerChannelInitializer()); //

            //启动server,并设置8088为启动端口,启动方式为同步
            ChannelFuture channelFuture = serverBootstrap.bind(host, port).sync();

            //监听关闭的channel,关闭方式为同步
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    /**
     * 入口函数
     */
    public static void main(String[] args) {
        new HttpServerBootstrap("127.0.0.1", 8088).start(); //启动netty服务器
    }
}
