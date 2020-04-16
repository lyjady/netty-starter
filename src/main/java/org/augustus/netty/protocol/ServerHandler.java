package org.augustus.netty.protocol;

import com.google.protobuf.Message;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;

/**
 * @author LinYongJin
 * @date 2020/4/16 21:07
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MessageProtocol protocol = (MessageProtocol) msg;
        System.out.println(ctx.channel().remoteAddress() + "发送了长度为" + protocol.getLength() + "的消息, 内容: " + new String(protocol.getData(), StandardCharsets.UTF_8));
        MessageProtocol message = new MessageProtocol();
        String content = "收到了老铁!~~~~";
        message.setData(content.getBytes()).setLength(content.getBytes().length);
        ctx.channel().writeAndFlush(message);
    }
}
