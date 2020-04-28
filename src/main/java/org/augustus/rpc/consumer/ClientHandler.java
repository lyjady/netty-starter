package org.augustus.rpc.consumer;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;

/**
 * @author LinYongJin
 * @date 2020/4/28 21:44
 */
public class ClientHandler extends ChannelInboundHandlerAdapter implements Callable {

    private ChannelHandlerContext context = null;

    private String result;

    private String param;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.context = ctx;
    }

    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        result = msg.toString();
        notify();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().close();
        cause.printStackTrace();
    }

    @Override
    public synchronized Object call() throws Exception {
        context.writeAndFlush(Unpooled.copiedBuffer(param, StandardCharsets.UTF_8));
        wait();
        return result;
    }

    public ClientHandler setParam(String param) {
        this.param = param;
        return this;
    }
}
