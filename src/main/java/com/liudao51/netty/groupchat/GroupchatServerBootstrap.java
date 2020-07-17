package com.liudao51.netty.groupchat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioChannelOption;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 群聊服务端Bootstrap
 */
public class GroupchatServerBootstrap {
    private final String host;
    private final Integer port;

    public GroupchatServerBootstrap(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    /**
     * 服务端启动方法
     *
     * @throws Exception
     */
    public void start() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(NioChannelOption.SO_BACKLOG, 128)
                    .childOption(NioChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new GroupchatChannelInitializer());

            ChannelFuture future = serverBootstrap.bind(host, port).sync();

            System.out.println("服务端[" + future.channel().localAddress() + "]启动成功...");

            //关闭通道
            future.channel().closeFuture().sync();

        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    /**
     * 入口函数
     */
    public static void main(String[] args) {
        try {
            new GroupchatServerBootstrap("127.0.0.1", 8088).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
