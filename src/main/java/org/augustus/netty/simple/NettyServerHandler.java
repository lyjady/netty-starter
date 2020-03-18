package org.augustus.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * 自定义的服务端handler, 需要继承Netty的HandlerAdapter, 这样才有效果
 *
 * @author LinYongJin
 * @date 2020/3/18 20:35
 */
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
        System.out.println("Thread Name: " + Thread.currentThread().getName());
        Channel channel = ctx.channel();
        ChannelPipeline pipeline = channel.pipeline();
        // 普通的任务提交到TaskQueue
        ctx.channel().eventLoop().execute(() -> {
            System.out.println("TaskQueue Thread Name: " + Thread.currentThread().getName());
            try {
                Thread.sleep(5 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ctx.channel().writeAndFlush(Unpooled.copiedBuffer("TaskQueue1", StandardCharsets.UTF_8));
        });
        ctx.channel().eventLoop().execute(() -> {
            System.out.println("TaskQueue Thread Name: " + Thread.currentThread().getName());
            try {
                Thread.sleep(10 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ctx.channel().writeAndFlush(Unpooled.copiedBuffer("TaskQueue2", StandardCharsets.UTF_8));
        });
        //延时任务提交到scheduleTaskQueue(延迟队列类似将线程睡眠, 但是如果TaskQueue与ScheduleTaskQueue中都有任务, 那么TaskQueue与ScheduleTaskQueue中的任务将会同时执行, 具体每个Queue中的任务再按照队列的顺序执行)
        ctx.channel().eventLoop().schedule(() -> {
            System.out.println("TaskQueue Thread Name: " + Thread.currentThread().getName());
            ctx.channel().writeAndFlush(Unpooled.copiedBuffer("ScheduleTaskQueue", StandardCharsets.UTF_8));
        }, 20, TimeUnit.SECONDS);
        // ByteBuf是Netty提供的, 类似于ByteBuffer但是效率更高
        ByteBuf buf = (ByteBuf) msg;
        String remoteAddress = ctx.channel().remoteAddress().toString();
        String message = buf.toString(StandardCharsets.UTF_8);
        System.out.println("来自客户端" + remoteAddress + "的消息: " + message);
        for (SocketChannel socketChannel : NettyServer.socketChannels) {
            if (socketChannel != ctx.channel()) {
                socketChannel.writeAndFlush(Unpooled.copiedBuffer(remoteAddress + ": " + message, StandardCharsets.UTF_8));
            }
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
        ctx.channel().writeAndFlush(Unpooled.copiedBuffer("服务器收到你的消息了, 装发给其他人!!!", StandardCharsets.UTF_8));
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
