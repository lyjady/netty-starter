package org.augustus.netty.protocol;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;

/**
 * @author LinYongJin
 * @date 2020/4/16 21:22
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 5; i++) {
            MessageProtocol messageProtocol = new MessageProtocol();
            String content = "成功破解PornHub的下载机制, 一起来下载吧~~~~";
            messageProtocol.setLength(content.getBytes().length).setData(content.getBytes());
            ctx.channel().writeAndFlush(messageProtocol);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MessageProtocol protocol = (MessageProtocol) msg;
        System.out.println("服务器发送了长度为" + protocol.getLength() + "的消息, 内容: " + new String(protocol.getData(), StandardCharsets.UTF_8));
    }
}
