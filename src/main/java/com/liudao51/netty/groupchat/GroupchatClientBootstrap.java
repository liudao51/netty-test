package com.liudao51.netty.groupchat;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

import java.util.Scanner;

/**
 * 客户端Bootstrap
 */
public class GroupchatClientBootstrap {
    private final String host;
    private final Integer port;

    public GroupchatClientBootstrap(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws Exception {
        //客户端只需要定义一个线程组，而服务端需要定义bossGroup,workerGroup两个线程组
        EventLoopGroup clientGroup = new NioEventLoopGroup();

        try {
            //客户端要用Bootstrap，而服务端要用ServerBootstrap
            Bootstrap clientBootstrap = new Bootstrap();

            clientBootstrap.group(clientGroup)
                    .channel(NioSocketChannel.class) //客户端要用NioSocketChannel，而服务端要用NioServerSocketChannel
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new GroupchatClientChannelInitializer());

            ChannelFuture future = clientBootstrap.connect(host, port).sync();

            System.out.println("客户端[" + future.channel().localAddress() + "]启动成功...");


            //发送消息（使用Scanner+System.in从键盘获取输入数据）
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {
                String msg = scanner.nextLine();
                future.channel().writeAndFlush(Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8));
            }

            //关闭通道
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
            new GroupchatClientBootstrap("127.0.0.1", 8088).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
