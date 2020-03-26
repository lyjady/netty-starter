package org.augustus.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @author LinYongJin
 * @date 2020/3/26 20:38
 */
public class NettyByteBuf {

    public static void main(String[] args) {
        // ByteBuf是Netty提供的一个数据容器, 类似于NIO的ByteBuffer, 但是不需要记性读写的反转(flip()), 底层是数组
        // ByteBuf维护了readerIndex, writerIndex, capacity
        // readerIndex: 代表从ByteBuf中读取数据时返回readerIndex索引位置的数据
        // writerIndex: 代表当数据写入ByteBuf时写入到writerIndex所在的索引位置
        // capacity: 数据容器的容量
        // 0 ~ readerIndex: 已经读取的数据, readerIndex ~ writerIndex: 未读取的数据, 0 ~ writerIndex: 以写入的数据, writerIndex ~ capacity: 未写入数据的区域
        ByteBuf buf = Unpooled.buffer(10);
        for (int i = 0; i < buf.capacity(); i++) {
            // 写入数据, 每写入一个writerIndex就会+1
            buf.writeByte(i);
        }
        for (int i = 0; i < buf.capacity(); i++) {
            // 读取数据数据, getByte()这个方法不会使readerIndex发生变化
            System.out.println(buf.getByte(i));
        }
        for (int i = 0; i < buf.capacity(); i++) {
            // 读取内容会改变readerIndex
            System.out.println(buf.readByte());
        }
        // 第二种构造方式, 输入字符序列或者字节数组与编码类型来进行构造
        ByteBuf byteBuf = Unpooled.copiedBuffer("丢雷人摸, 李秀猪", StandardCharsets.UTF_8);
        System.out.println("capacity: " + byteBuf.capacity());
        System.out.println("readerIndex: " + byteBuf.readerIndex());
        System.out.println("writerIndex: " + byteBuf.writerIndex());
        // 可读取的数量(writerIndex - readerIndex)
        System.out.println("readable count: " + byteBuf.readableBytes());
        // 底层数组
        System.out.println("array: " + Arrays.toString(byteBuf.array()));
        // 是否有数据
        System.out.println("has array: " + byteBuf.hasArray());
        // 读取ByteBuf中的字节序列。getCharSequence(起始索引, 读取的长度, 指定的字符集)
        System.out.println(byteBuf.getCharSequence(0, 11, StandardCharsets.UTF_8));
    }
}
