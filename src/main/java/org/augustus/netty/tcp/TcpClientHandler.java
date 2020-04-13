package org.augustus.netty.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;

/**
 * @author LinYongJin
 * @date 2020/4/13 21:31
 */
public class TcpClientHandler extends ChannelInboundHandlerAdapter {

    private int count;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 10; i++) {
            ctx.writeAndFlush(Unpooled.copiedBuffer("TCP粘包与拆包" + i, StandardCharsets.UTF_8));
        }
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
}
