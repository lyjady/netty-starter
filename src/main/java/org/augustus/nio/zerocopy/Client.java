package org.augustus.nio.zerocopy;

import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
 * @author LinYongJin
 * @date 2020/3/10 21:02
 */
public class Client {

    public static void main(String[] args) throws Exception{
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 8080));
        FileChannel fileChannel = new FileInputStream("filename").getChannel();
        long startTime = System.currentTimeMillis();
        // 在Linux下transferTo一次就可以完成传输.但是在Windows下一次只能拷贝8Mb的内容
        long length = fileChannel.transferTo(0, fileChannel.size(), socketChannel);
        System.out.println("传送的字节数: " + length + ", 花费的时间: " + (System.currentTimeMillis() - startTime));
        fileChannel.close();
    }
}
