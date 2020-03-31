package org.augustus.netty.heartbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author LinYongJin
 * @date 2020/3/31 21:45
 */
public class Server {

    public static void main(String[] args) {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        try {
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    // 在BossGroup添加日志处理器
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 加入一个Netty提供的IdleStateHandler
                            //new IdleStateHandler(readerIdleTimeSeconds, writerIdleTimeSeconds, allIdleTimeSeconds);
                            // IdleStateHandler是Netty提供的处理空闲状态的处理器, 他会执行一个IdleStateEvent(状态事件), 当这个channel在一段时间内没有执行读、写、读写.
                            // 主要用于客户端在服务器没有感知的情况下失去连接(传统的handlerRemove方法无法执行), 用这个来检测客户端是否存活
                            // readerIdleTimeSeconds: 读空闲的时间即在指定的时间内没有发生读事件就是读空闲
                            // writerIdleTimeSeconds: 写空闲的时间即在指定的时间内没有发生写事件就是写空闲
                            // allIdleTimeSeconds: 读写空闲的时间即在指定的时间内没有发生读写事件就是读写空闲
                            // 当这个IdleStateEvent触发后, 就会传递给Pipeline的下一个Handler去处理(通过调用下一个Handler的useEventTriggered, 在该方法中处理这个发生的StateEvent)
                            pipeline.addLast(new IdleStateHandler(3, 5, 7, TimeUnit.SECONDS));
                            // 加入一个对空闲检测进一步处理的Handler(自定义)
                            pipeline.addLast(new ServerHandler());
                        }
                    });
            ChannelFuture sync = serverBootstrap.bind("127.0.0.1", 8080).sync();
            sync.addListener(future -> {
                if (future.isSuccess()) {
                    System.out.println("服务器启动成功~~~");
                }
            });
            sync.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
