package org.augustus.netty.protobuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;
import java.util.Random;

/**
 * 自定义客户端的Handler, 同样要继承Netty的Handler
 *
 * @author LinYongJin
 * @date 2020/3/18 21:06
 */
//@Slf4j
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * 当通道就绪就会执行这个方法
     *
     * @param ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        int random = new Random().nextInt(3);
        if (random == 0) {
            PeoplePoJo.People.Builder boss = PeoplePoJo.People.newBuilder().setDataType(PeoplePoJo.People.DataType.BossType).setBoss(PeoplePoJo.Boss.newBuilder().setId(1).setName("boss").build());
            ctx.writeAndFlush(boss).sync();
        } else {
            PeoplePoJo.People.Builder worker = PeoplePoJo.People.newBuilder().setDataType(PeoplePoJo.People.DataType.WorkerType).setWorker(PeoplePoJo.worker.newBuilder().setNo(1).setNickname("worker").build());
            ctx.writeAndFlush(worker).sync();
        }
    }

    /**
     * 读取服务器的消息
     *
     * @param ctx
     * @param msg
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("服务器的消息: " + byteBuf.toString(StandardCharsets.UTF_8));
    }

    /**
     * 异常处理
     *
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.channel().close();
    }
}
