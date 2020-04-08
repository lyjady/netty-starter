package org.augustus.netty.protobuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * 自定义的服务端handler, 需要继承Netty的HandlerAdapter, 这样才有效果
 *
 * @author LinYongJin
 * @date 2020/3/18 20:35
 */
//@Slf4j
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * @param ctx
     */
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        String remoteAddress = ctx.channel().remoteAddress().toString();
        System.out.println(remoteAddress + "已连接.......");
    }

    /**
     * 开始读从客户端发送来的数据
     *
     * @param ctx
     * @param msg
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        PeoplePoJo.People people = (PeoplePoJo.People) msg;
        if ((people.getDataType() == PeoplePoJo.People.DataType.BossType)) {
            System.out.println("boss: " + people.getBoss().getId() + ": " + people.getBoss().getName());
        } else {
            System.out.println("worker: " + people.getWorker().getNo() + ": " + people.getWorker().getNickname());
        }
    }

    /**
     * 读取完成之后执行的方法
     *
     * @param ctx
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 将数据写入缓冲并刷新(刷新就是将数据写到通道)
        ChannelFuture channelFuture = ctx.channel().writeAndFlush(Unpooled.copiedBuffer("服务器收到你的消息了, 装发给其他人!!!", StandardCharsets.UTF_8)).sync();
        channelFuture.addListener(future -> {
            if (future.isSuccess()) {
                if (future.isSuccess()) {
                    System.out.println("向客户端发送消息监听器: 发送给客户端成功!!!");
                } else {
                    System.out.println("向客户端发送消息监听器: 发送给客户端失败~~~");
                }
            }
        });
    }

    /**
     *
     * @param ctx
     */
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        String remoteAddress = ctx.channel().remoteAddress().toString();
        System.out.println(remoteAddress + "关闭了连接");
    }

    /**
     * 发生异常时执行的方法
     *
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 打印错误信息
        cause.printStackTrace();
        // 关闭通道
        ctx.channel().closeFuture();
        String remoteAddress = ctx.channel().remoteAddress().toString();
        System.out.println(remoteAddress + "发生了异常, 连接关闭");
    }
}
