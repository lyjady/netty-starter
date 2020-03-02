package org.augustus.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * @author LinYongJin
 * @date 2020/3/2 21:17
 */
public class NioServer {

    public static void main(String[] args) throws IOException {
        // 1.创建ServerSocketChannel -> ServerSocket并绑定端口设置非阻塞
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(8080));
        serverSocketChannel.configureBlocking(false);
        // 2.创建Selector
        Selector selector = Selector.open();
        // 3.将ServerSocketChannel注册到Selector并注册为接收事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            // 监听时间发生设置超时时间为1s, 1s内没有事件发生则不再监听
            if (selector.select(1000) == 0) {
                continue;
            }
            // 有事件发生循环遍历SelectorKeys去查询是哪个channel发生事件
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while (keyIterator.hasNext()) {
                // 得到selectorKey
                SelectionKey selectionKey = keyIterator.next();
                // 判断事件类型
                if (selectionKey.isAcceptable()) {
                    // 如果是连接事件那么说明有一个客户端连接成功创建一个SocketChannel注册到Selector中并注册为READ事件并关联一个Buffer
                    System.out.println("有一个客户端连接");
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }
                if (selectionKey.isReadable()) {
                    // 如果为读取事件
                    // 得到SocketChannel
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    // 得到关联的Buffer
                    ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
                    // 数据读到Buffer
                    socketChannel.read(buffer);
                    buffer.clear();
                    System.out.println("客户端的数据: " + new String(buffer.array(), StandardCharsets.UTF_8));
                }
                // 处理完移除selectorKey避免多线程重复操作
                keyIterator.remove();
            }
        }
    }
}
