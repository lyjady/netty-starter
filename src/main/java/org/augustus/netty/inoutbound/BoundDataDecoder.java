package org.augustus.netty.inoutbound;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author LinYongJin
 * @date 2020/4/8 21:54
 */
public class BoundDataDecoder extends ByteToMessageDecoder {

    /**
     * @param ctx
     * @param in  入站的ByteBuf
     * @param out
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 当ByteBuf中的值大于等于8时才满足long类型的字节数
        if (in.readableBytes() >= 8) {
            out.add(in.readLong());
        }
    }
}
