package org.augustus.netty.protocol;

/**
 * @author LinYongJin
 * @date 2020/4/16 20:43
 */
public class MessageProtocol {

   private int length;

   private byte[] data;

    public int getLength() {
        return length;
    }

    public MessageProtocol setLength(int length) {
        this.length = length;
        return this;
    }

    public byte[] getData() {
        return data;
    }

    public MessageProtocol setData(byte[] data) {
        this.data = data;
        return this;
    }
}
