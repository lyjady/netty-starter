package org.augustus.netty.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author LinYongJin
 * @date 2020/4/16 20:44
 */
public class Encoder extends MessageToByteEncoder<MessageProtocol> {

    @Override
    protected void encode(ChannelHandlerContext ctx, MessageProtocol msg, ByteBuf out) throws Exception {
        System.out.println("Encoder.encode");
        out.writeInt(msg.getLength());
        out.writeBytes(msg.getData());
    }
}
