package org.augustus.nio.channel;

import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * @author LinYongJin
 * @date 2020/2/26 20:50
 */
public class FileChannelBasic {

    /**
     * read from 1.txt
     * @throws IOException
     */
    @Test
    public void read() throws IOException {
        FileInputStream fis = new FileInputStream("file/1.txt");
        FileChannel channel = fis.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(8);
        StringBuffer sb = new StringBuffer();
        int length = 0;
        while ((length = channel.read(byteBuffer)) != -1) {
            sb.append(new String(byteBuffer.array(), 0, length, StandardCharsets.UTF_8));
            byteBuffer.clear();
        }
        System.out.println(sb);
        fis.close();
    }

    /**
     * write to 2.txt
     * @throws IOException
     */
    @Test
    public void write() throws IOException {
        FileOutputStream fos = new FileOutputStream("file/2.txt");
        FileChannel channel = fos.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.wrap("仿佛兮若轻云之蔽月, 飘摇兮若流风之回雪".getBytes());
        int length = channel.write(byteBuffer);
        System.out.println("length: " + length);
        fos.close();
    }

    /**
     * copy 3.txt to 4.txt
     */
    @Test
    public void copy() throws IOException{
        FileInputStream fis = new FileInputStream("file/3.txt");
        FileOutputStream fos = new FileOutputStream("file/4.txt");
        FileChannel channelC = fis.getChannel();
        FileChannel channelV = fos.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while (channelC.read(buffer) != -1) {
            buffer.flip();
            channelV.write(buffer);
            buffer.clear();
        }
    }

    @Test
    public void transferFrom() throws IOException {
        File file = new File("file/1.jpg");
        FileInputStream fis = new FileInputStream(file);
        FileOutputStream fos = new FileOutputStream("file/2.jpg");
        FileChannel srcChannel = fis.getChannel();
        FileChannel destChannel = fos.getChannel();
        destChannel.transferFrom(srcChannel, 0, file.length());
    }

    @Test
    public void transferTo() throws IOException {
        File file = new File("file/1.jpg");
        FileInputStream fis = new FileInputStream(file);
        FileOutputStream fos = new FileOutputStream("file/3.jpg");
        FileChannel srcChannel = fis.getChannel();
        FileChannel destChannel = fos.getChannel();
        srcChannel.transferTo(0, file.length(), destChannel);
    }
}
