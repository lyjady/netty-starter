package org.augustus.netty.inoutbound;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author LinYongJin
 * @date 2020/4/13 20:41
 */
public class BoundNettyClient {

    public static void main(String[] args) {
        // 创建客户端的线程组
        EventLoopGroup eventExecutors = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventExecutors)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 解码器
                            ch.pipeline().addLast(new BoundDataDecoder());
                            // 编码器
                            ch.pipeline().addLast(new BoundDataEncoder());
                            // handler
                            ch.pipeline().addLast(new BoundClientHandler());
                        }
                    });

            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8080).sync();
            channelFuture.addListener(future -> {
                if (future.isSuccess()) {
                    System.out.println("客户端启动监听器: 客户端启动成功");
                }
            });
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            eventExecutors.shutdownGracefully();
        }
    }
}
