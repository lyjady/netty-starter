package org.augustus.netty.inoutbound;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.List;

/**
 * @author LinYongJin
 * @date 2020/4/13 20:42
 */
public class BoundDataEncoder extends MessageToByteEncoder<Long> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Long msg, ByteBuf out) throws Exception {
        System.out.println("BoundDataEncoder.encode");
        out.writeLong(msg);
    }
}
