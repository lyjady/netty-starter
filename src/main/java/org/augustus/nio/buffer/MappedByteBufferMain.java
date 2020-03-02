package org.augustus.nio.buffer;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author LinYongJin
 * @date 2020/3/2 20:36
 */
public class MappedByteBufferMain {

    public static void main(String[] args) throws Exception{
        // MappedByteBuffer直接在堆外内存(直接内存, 即操作系统的内存)来进行文件的修改, 不用将文件复制到Java程序中
        RandomAccessFile accessFile = new RandomAccessFile("file/5.txt", "rw");
        FileChannel channel = accessFile.getChannel();
        //MapMode.READ_WRITE: 使用的读写模式
        //0: 可以修改的起始位置
        //5: 最多修改的内存大小
        //如果文件中是字节那么直接就是指索引(不包含5)
        MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
        map.put(1, (byte) 'H');
        map.put(2, (byte) 'H');
        map.put(3, (byte) 'H');
        map.put(4, (byte) 'H');
        channel.close();
    }
}
