package org.augustus.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author LinYongJin
 * @date 2020/3/13 20:41
 */
public class NettyServer {

    public static CopyOnWriteArrayList<SocketChannel> socketChannels = new CopyOnWriteArrayList<>();

    public static void main(String[] args) throws InterruptedException {
        // 1.创建BossGroup和WorkerGroup这两个线程组
        // 指定EventLoopGroup线程组中的线程数量, 如果不写默认传0, 底层进行判断如果是0那么就新建CPU核数 * 2的线程数量
        // boosGroup只用来处理连接事件只要一个只要一个EventLoop就够了, workerGroup来处理客户端的读写事件使用默认的线程数量
        EventLoopGroup boosGroup = new NioEventLoopGroup(1);
        EventLoopGroup workgroup = new NioEventLoopGroup();
        // 2.创建服务端的启动对象, 配置参数
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(boosGroup, workgroup) // 设置两个线程组
                .channel(NioServerSocketChannel.class) // 服务器的通道类型
                .option(ChannelOption.SO_BACKLOG, 128) //设置线程队列等待连接的个数
                .childOption(ChannelOption.SO_KEEPALIVE, true) // 设置保持活动连接状态
                .childHandler(new ChannelInitializer<SocketChannel>() { // 给WorkGroup对应的管道设置处理器
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannels.add(socketChannel);
                        // 向管道中添加处理器
                        socketChannel.pipeline().addLast(new NettyServerHandler());
                    }
                });
        // 3.为服务器绑定端口并设置成同步的
        ChannelFuture channelFuture = bootstrap.bind(8080).sync();
        // 4.监听管道关闭事件
        channelFuture.channel().closeFuture().sync();
    }
}
