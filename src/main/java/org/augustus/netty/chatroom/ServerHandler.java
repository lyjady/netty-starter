package org.augustus.netty.chatroom;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author LinYongJin
 * @date 2020/3/31 20:53
 */
public class ServerHandler extends SimpleChannelInboundHandler<String> {

    /**
     * channel的集合, 用于存放客户端的SocketChannel
     * GlobalEventExecutor.INSTANCE: 是全局事件处理器, 是单例
     */
    public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 表示连接建立, 一旦连接, 第一个被执行
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        LocalDateTime now = LocalDateTime.now();
        String time = now.getHour() + ":" + now.getMinute() + ":" + now.getSecond();
        channels.writeAndFlush(time + " - " + ctx.channel().remoteAddress() + "上线");
        channels.add(ctx.channel());
    }

    /**
     * channel处于激活状态时执行
     *
     * @param ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LocalDateTime now = LocalDateTime.now();
        String time = now.getHour() + ":" + now.getMinute() + ":" + now.getSecond();
        System.out.println(time + " - " + ctx.channel().remoteAddress() + "上线");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        LocalDateTime now = LocalDateTime.now();
        String time = now.getHour() + ":" + now.getMinute() + ":" + now.getSecond();
        Channel socketChannel = ctx.channel();
        String message = time + " - " + socketChannel.remoteAddress() + ": " + msg;
        channels.forEach(channel -> {
            if (socketChannel != channel) {
                channel.writeAndFlush(message);
            } else {
                socketChannel.writeAndFlush(time + " - " + "发送了消息: " + msg);
            }
        });
    }

    /**
     * channel处于非激活状态时执行
     *
     * @param ctx
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        LocalDateTime now = LocalDateTime.now();
        String time = now.getHour() + ":" + now.getMinute() + ":" + now.getSecond();
        System.out.println(time + " - " + ctx.channel().remoteAddress() + "下线");
    }

    /**
     * 断开连接被触发, 这个方法会自动将该channel自动移除出channel group
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        LocalDateTime now = LocalDateTime.now();
        String time = now.getHour() + ":" + now.getMinute() + ":" + now.getSecond();
        channels.writeAndFlush(time + " - " + ctx.channel().remoteAddress() + "用户下线");
    }

    /**
     * 异常触发
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.channel().close();
    }
}
