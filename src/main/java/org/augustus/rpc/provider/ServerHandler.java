package org.augustus.rpc.provider;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;

/**
 * @author LinYongJin
 * @date 2020/4/28 21:24
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 要求没次发请求都是都必须以某个字符串开头 HelloService#hello#content
        String content = (String) msg;
        if (content.startsWith("HelloService#hello")) {
            String hello = new HelloServiceImpl().hello(content.substring(content.lastIndexOf("#") + 1));
            ctx.writeAndFlush(Unpooled.copiedBuffer(hello, StandardCharsets.UTF_8));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().close();
        cause.printStackTrace();
    }
}
