package org.augustus.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * 自定义客户端的Handler, 同样要继承Netty的Handler
 *
 * @author LinYongJin
 * @date 2020/3/18 21:06
 */
//@Slf4j
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * 当通道就绪就会执行这个方法
     *
     * @param ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ChannelFuture channelFuture = ctx.writeAndFlush(Unpooled.copiedBuffer("I am inevitable", StandardCharsets.UTF_8)).sync();
        channelFuture.addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("向服务端发送消息监听器: 发送给服务器成功!!!");
            } else {
                System.out.println("向服务端发送消息监听器: 发送给服务器失败~~~");
            }
        });
    }

    /**
     * 读取服务器的消息
     *
     * @param ctx
     * @param msg
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("服务器的消息: " + byteBuf.toString(StandardCharsets.UTF_8));
    }

    /**
     * 异常处理
     *
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.channel().close();
    }
}
