package com.liudao51.netty.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Echo 服务器端
 */
public class EchoServerBootstrap {

    private final String host;
    private final Integer port;

    public EchoServerBootstrap(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    /**
     * netty服务器启动方法
     */
    public void start() throws Exception {
        EventLoopGroup boosGroup = new NioEventLoopGroup(); //boss线程组
        EventLoopGroup workerGroup = new NioEventLoopGroup(); //worker线程组

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boosGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) //指定所使用的NIO传输Channel
                    .option(ChannelOption.SO_BACKLOG, 100)  //设置临时存放已完成三次握手的请求的队列的最大长度（如果未设置或所设置的值小于1，Java将使用默认值50）。
                    .childOption(ChannelOption.SO_KEEPALIVE, true) //是否启用心跳保活机制。在双方TCP套接字建立连接后（即都进入ESTABLISHED状态）并且在两个小时左右上层没有任何数据传输的情况下，这套机制才会被激活。

                    //设置ChannelInitializer
                    //ChannelInitializer是一个特殊的ChannelInboundHandler，是用于在某个Channel注册到EventLoop后，对这个Channel进行初始化（如：配置管道pipeline中想要添加的handler）。
                    //ChannelInitializer虽然会在一开始会被注册到Channel相关的pipeline里，但是在初始化完成之后，ChannelInitializer会将自己从pipeline中移除，不会影响后续的操作。
                    .childHandler(new EchoServerChannelInitializer());

            ChannelFuture future = serverBootstrap.bind(host, port).sync(); //绑定服务器及端口，阻塞当前线程直到绑定完成
            System.out.println("服务端[" + future.channel().localAddress() + "]启动成功...");
            future.channel().closeFuture().sync(); //阻塞当前线程直到Channel关闭
        } finally {
            //释放所有线程资源
            boosGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    /**
     * 入口函数
     */
    public static void main(String[] args) {
        try {
            new EchoServerBootstrap("127.0.0.1", 8088).start(); //启动netty服务器
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
