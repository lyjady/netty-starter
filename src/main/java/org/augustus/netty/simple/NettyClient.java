package org.augustus.netty.simple;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.awt.print.Pageable;

/**
 * @author LinYongJin
 * @date 2020/3/18 20:56
 */
public class NettyClient {

    public static void main(String[] args) throws InterruptedException {
        // 创建客户端的线程组
        EventLoopGroup eventExecutors = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            // 设置启动参数
            bootstrap.group(eventExecutors)
                    // 设置客户端的通道实现
                    .channel(NioSocketChannel.class)
                    // 为客户端设置Handler
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyClientHandler());
                        }
                    });

            // 启动客户端连接服务器, 并设置成同步的
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8080).sync();
            channelFuture.addListener(future -> {
                if (future.isSuccess()) {
                    System.out.println("客户端启动监听器: 客户端启动成功");
                }
            });
            // 设置通道关闭的监听
            channelFuture.channel().closeFuture().sync();
        } finally {
            eventExecutors.shutdownGracefully();
        }
    }
}
