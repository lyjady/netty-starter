package org.augustus.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.augustus.netty.heartbeat.ServerHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author LinYongJin
 * @date 2020/3/31 22:32
 */
public class WsServer {
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
                            // 因为基于Http协议, 使用Http的编解码器
                            pipeline.addLast(new HttpServerCodec());
                            // 是以块方式写, 添加ChunkedWriteHandler
                            pipeline.addLast(new ChunkedWriteHandler());
                            // Http在传输过程中是分段的, HttpObjectAggregator就是将多段聚合(如果数据量大那么浏览器就会发送多次Http请求)
                            pipeline.addLast(new HttpObjectAggregator(8192));
                            // 对于WebSocket它的数据是以帧frame形式传递,
                            // 浏览器请求时ws://localhost:8080/{path}, path表示请求的uri, 匹配new WebSocketServerProtocolHandler({path})里面的path
                            // 这个Handler还可以将Http协议升级成WebSocket, 保持长连接。
                            pipeline.addLast(new WebSocketServerProtocolHandler("/netty"));
                            // 自定义的Handler处理业务逻辑
                            pipeline.addLast(new WsServerHandler());
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
