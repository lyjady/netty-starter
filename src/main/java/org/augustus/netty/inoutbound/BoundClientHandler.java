package org.augustus.netty.inoutbound;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author LinYongJin
 * @date 2020/4/13 20:47
 */
public class BoundClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("发送~~~~~~~~~");
        ctx.writeAndFlush("123121L");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.channel().closeFuture();
    }
}
