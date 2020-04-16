package org.augustus.netty.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * @author LinYongJin
 * @date 2020/4/16 20:49
 */
public class Decoder extends ReplayingDecoder<Void> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("Decoder.decode");
        int length = in.readInt();
        byte[] bytes = new byte[length];
        in.readBytes(bytes);
        MessageProtocol protocol = new MessageProtocol();
        protocol.setLength(length).setData(bytes);
        out.add(protocol);
    }
}
