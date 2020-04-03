package org.augustus.netty.websocket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.time.LocalDateTime;

/**
 * @author LinYongJin
 * @date 2020/4/3 20:42
 */
public class WsServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // channel的id, asLongText是完整的不会重复, asShortText只是部分可能会重复
        System.out.println(ctx.channel().id().asLongText());
        System.out.println(ctx.channel().id().asShortText());
        System.out.println("客户端: " + ctx.channel().remoteAddress() + "连接服务器");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        LocalDateTime now = LocalDateTime.now();
        String time = now.getHour() + ":" + now.getMinute() + ":" + now.getSecond();
        System.out.println(ctx.channel().remoteAddress() + ": " + msg.text());
        ctx.writeAndFlush(new TextWebSocketFrame("服务器 - " + time + ": " + msg.text()));
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端: " + ctx.channel().remoteAddress() + "断开连接");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().closeFuture();
        cause.printStackTrace();
    }
}
