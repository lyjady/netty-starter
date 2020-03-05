package org.augustus.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author LinYongJin
 * @date 2020/3/5 20:49
 */
public class NioClient {

    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 8080);
        socketChannel.configureBlocking(false);
        if (!socketChannel.connect(inetSocketAddress)) {
            while (!socketChannel.finishConnect()) {
                System.out.println("连接中......");
            }
        }
        ByteBuffer byteBuffer = ByteBuffer.wrap("I am your father".getBytes());
        socketChannel.write(byteBuffer);
        System.in.read();
    }
}
