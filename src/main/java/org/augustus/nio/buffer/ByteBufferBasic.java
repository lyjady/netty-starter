package org.augustus.nio.buffer;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * @author LinYongJin
 * @date 2020/2/26 20:31
 */
public class ByteBufferBasic {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(18);
        for (byte i = 1; i < 9; i++) {
            buffer.put(i);
        }
        System.out.println(buffer.hasArray());
        System.out.println(Arrays.toString(buffer.array()));
        System.out.println(buffer.position());
        buffer.flip();
        System.out.println(buffer.get());
        System.out.println(buffer.position());
        System.out.println(buffer.get(2));
        System.out.println("-----");
        buffer.put(3, (byte) 19);
        System.out.println(buffer.get());
        System.out.println(buffer.get());
        System.out.println(buffer.get());
        System.out.println(buffer.get());
        System.out.println(buffer.get());
        System.out.println(buffer.get());
        System.out.println(buffer.array().length);
    }
}
