package org.augustus.nio.buffer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * @author LinYongJin
 * @date 2020/3/2 20:46
 */
public class ScatteringAndGathering {

    public static void main(String[] args) throws IOException {
        // NIO还支持多个Buffer(Buffer数组)合作来完成读写。当一个buffer的容量不够可以使用一个Buffer数组来进行读写,那数据就会分批放到数组中去
        // 同样也可以从多个Buffer中读取之前写的数据
        // 1.创建ServerSocketChannel并绑定端口
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(8080));
        // 2.创建Buffer数组
        ByteBuffer[] byteBuffers = {ByteBuffer.allocate(5), ByteBuffer.allocate(3)};
        // 3.等待客户端连接
        SocketChannel socketChannel = serverSocketChannel.accept();
        System.out.println("收到客户端的连接: " + socketChannel.getRemoteAddress());
        // 4.循环读取
        int maxLength = 8;
        while (true) {
            int byteRead = 0;
            int byteWrite = 0;
            while (byteRead < maxLength) {
                long read = socketChannel.read(byteBuffers);
                byteRead += read;
                System.out.println("byteRead: " + byteRead);
                Arrays.stream(byteBuffers).map(byteBuffer -> "position: " + byteBuffer.position() + ", limit: " + byteBuffer.limit()).forEach(System.out::println);
            }
            Arrays.stream(byteBuffers).forEach(ByteBuffer::flip);
            while (byteWrite < maxLength) {
                long write = socketChannel.write(byteBuffers);
                byteWrite += write;
                System.out.println("byteWrite: " + byteWrite);
            }
            Arrays.stream(byteBuffers).forEach(ByteBuffer::clear);
            System.out.println("byteRead: " + byteRead + ", byteWrite: " + byteWrite);
        }
    }
}
