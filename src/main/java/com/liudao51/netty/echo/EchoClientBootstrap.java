package com.liudao51.netty.echo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Scanner;

/**
 * Echo客户端
 */
public class EchoClientBootstrap {
    private final String host;
    private final Integer port;


    public EchoClientBootstrap(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    /**
     * netty客户端启动方法
     */
    public void start() throws Exception {
        EventLoopGroup clientGroup = new NioEventLoopGroup(1);
        Scanner scanner = null;
        try {
            Bootstrap clientBootstrap = new Bootstrap();
            clientBootstrap.group(clientGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new EchoClientChannelInitializer());

            ChannelFuture future = clientBootstrap.connect(host, port).sync();  //连接服务器及端口，阻塞当前线程直到连接完成
            System.out.println("客户端[" + future.channel().localAddress() + "]启动成功...");
            future.channel().closeFuture().sync();
        } finally {
            clientGroup.shutdownGracefully();
        }
    }

    /**
     * 入口函数
     */
    public static void main(String[] args) {
        try {
            new EchoClientBootstrap("127.0.0.1", 8088).start(); //启动netty客户端
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
