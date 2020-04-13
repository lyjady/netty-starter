package org.augustus.netty.tcp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.augustus.netty.inoutbound.BoundDataDecoder;
import org.augustus.netty.inoutbound.BoundDataEncoder;
import org.augustus.netty.inoutbound.BoundServerHandler;
import org.augustus.netty.simple.NettyServerHandler;

/**
 * @author LinYongJin
 * @date 2020/4/13 21:19
 */
public class TcpNettyServer {

    public static void main(String[] args) {
        EventLoopGroup boosGroup = new NioEventLoopGroup(1);
        EventLoopGroup workgroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boosGroup, workgroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline channelPipeline = socketChannel.pipeline();
                            channelPipeline.addLast(new TcpServerHandler());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.bind(8080).sync();
            channelFuture.addListener(future -> {
                if (future.isSuccess()) {
                    System.out.println("服务器启动监听器: 服务器启动成功!!!");
                }
            });
            // 4.监听管道关闭事件
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            boosGroup.shutdownGracefully();
            workgroup.shutdownGracefully();
        }
    }
}
