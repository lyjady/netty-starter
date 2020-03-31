package org.augustus.netty.heartbeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author LinYongJin
 * @date 2020/3/31 21:59
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    /**
     *
     * @param ctx channelHandlerContext
     * @param evt 发生的事件
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent stateEvent = (IdleStateEvent) evt;
            IdleState state = stateEvent.state();
            switch (state) {
                case READER_IDLE:
                    System.out.println("读空闲~~~");
                    break;
                case WRITER_IDLE:
                    System.out.println("写空闲~~~");
                    break;
                case ALL_IDLE:
                    System.out.println("读写空闲~~~");
                    break;
                default:
                    System.out.println("不空闲~~~");
                    break;
            }
        }
    }
}
