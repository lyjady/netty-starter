package org.augustus.netty.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * @author LinYongJin
 * @date 2020/4/13 21:29
 */
public class TcpServerHandler extends ChannelInboundHandlerAdapter {

    private int count;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] buffer = new byte[((ByteBuf) msg).readableBytes()];
        byteBuf.readBytes(buffer);
        System.out.println(new String(buffer, StandardCharsets.UTF_8));
        System.out.println("第" + ++count + "次");
        System.out.println();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer(UUID.randomUUID().toString() + "--", StandardCharsets.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().closeFuture();
    }
}
