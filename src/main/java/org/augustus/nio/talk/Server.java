package org.augustus.nio.talk;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

/**
 * @author LinYongJin
 * @date 2020/3/5 20:58
 */
public class Server {

    private ServerSocketChannel serverSocketChannel;

    private static final Integer PORT = 8080;

    private Selector selector;

    public Server() {
        try {
            // 实例化ServerSocketChannel并向Selector注册
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(8080));
            serverSocketChannel.configureBlocking(false);
            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始监听
     */
    public void startup() {
        try {
            while (true) {
                if (selector.selectNow() != 0) {
                    Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                    while (keyIterator.hasNext()) {
                        SelectionKey selectionKey = keyIterator.next();
                        if (selectionKey.isAcceptable()) {
                            SocketChannel socketChannel = serverSocketChannel.accept();
                            System.out.println("客户端: " + socketChannel.getRemoteAddress() + "上线");
                            socketChannel.configureBlocking(false);
                            socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                        }
                        if (selectionKey.isReadable()) {
                            read(selectionKey);
                        }
                        keyIterator.remove();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void read(SelectionKey selectionKey) throws IOException {
        SocketAddress socketAddress = null;
        SocketChannel socketChannel = null;
        try {
            socketChannel = (SocketChannel) selectionKey.channel();
            socketAddress = socketChannel.getRemoteAddress();
            ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
            int length = socketChannel.read(buffer);
            buffer.clear();
            String data = new String(buffer.array(), 0, length, StandardCharsets.UTF_8);
            System.out.println("来自" + socketAddress + "客户端的消息: " + data);
            sendToOther(data, socketChannel);
        } catch (IOException e) {
            System.out.println("客户端: " + socketAddress + "离线");
            selectionKey.cancel();
            socketChannel.close();
        }
    }

    /**
     * 转发给其他用户
     */
    public void sendToOther(String message, SocketChannel socketChannel) {
        Iterator<SelectionKey> keyIterator = selector.keys().iterator();
        try {
            SocketAddress remoteAddress = socketChannel.getRemoteAddress();
            while (keyIterator.hasNext()) {
                SelectionKey selectionKey = keyIterator.next();
                Channel channel = selectionKey.channel();
                if (channel instanceof SocketChannel && channel != socketChannel) {
                    ByteBuffer byteBuffer = ByteBuffer.wrap((remoteAddress + ": " + message).getBytes());
                    ((SocketChannel) channel).write(byteBuffer);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.startup();
    }
}
