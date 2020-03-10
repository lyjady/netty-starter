package org.augustus.nio.zerocopy;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author LinYongJin
 * @date 2020/3/10 20:56
 */
public class Server {

    public static void main(String[] args) throws Exception{
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(8080));
        ByteBuffer byteBuffer = ByteBuffer.allocate(4096);
        try {
            while (true) {
                SocketChannel socketChannel = serverSocketChannel.accept();
                int read = 0;
                while (read != -1) {
                    read = socketChannel.read(byteBuffer);
                }
                byteBuffer.rewind();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
