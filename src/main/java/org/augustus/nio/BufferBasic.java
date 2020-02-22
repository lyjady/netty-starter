package org.augustus.nio;

import java.nio.IntBuffer;

/**
 * @author LinYongJin
 * @date 2020/2/22 15:38
 */
public class BufferBasic {

    public static void main(String[] args) {
        intBuffer();
    }

    private static void intBuffer() {
        IntBuffer intBuffer = IntBuffer.allocate(10);
        // 向intBuffer中添加元素
        for (int i = 1; i < 11; i++) {
            intBuffer.put(i * 10);
        }
        // 向intBuffer中取出元素
        // 反转intBuffer write -> read
        intBuffer.flip();
        while (intBuffer.hasRemaining()) {
            System.out.println(intBuffer.get());
        }
    }
}
