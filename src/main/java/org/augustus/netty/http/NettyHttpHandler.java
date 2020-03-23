package org.augustus.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;
import java.nio.charset.StandardCharsets;

/**
 * @author LinYongJin
 * @date 2020/3/23 21:26
 * 1.SimpleChannelInboundHandler: 是ChannelInboundHandlerAdapter的子类
 * 2.HttpObject: 客户端与服务器端数据交互的类型, Netty把数据封装成HttpObject对象
 */
public class NettyHttpHandler extends SimpleChannelInboundHandler<HttpObject> {

    /**
     * 读取客户端数据
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {

        // 判断msg是不是HTtpRequest类型
        if (msg instanceof HttpRequest) {
            HttpRequest httpRequest = (HttpRequest) msg;
            URI uri = new URI(httpRequest.uri());
            if ("/favicon.ico".equals(uri.getPath())) {
                return;
            }
            System.out.println("pipeline hashcode: " + ctx.pipeline().hashCode());
            System.out.println("channel hashcode: " + ctx.channel().hashCode());
            System.out.println("handler hashcode: " + hashCode());
            // 回复信息给客户端(之前的服务器时TCP协议可以是一个简单的文本, Http服务器需要满足Http协议)
            ByteBuf buf = Unpooled.copiedBuffer("这是来自Netty写的Http服务器的消息", StandardCharsets.UTF_8);
            // 构建Response
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
            HttpHeaders headers = response.headers();
            // 设置相应头
            headers.set(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=utf-8");
            headers.set(HttpHeaderNames.CONTENT_LENGTH, buf.readableBytes());
            // 返回response
            ctx.writeAndFlush(response);
        }
    }
}
