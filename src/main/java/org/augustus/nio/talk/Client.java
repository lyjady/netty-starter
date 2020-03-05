package org.augustus.nio.talk;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * @author LinYongJin
 * @date 2020/3/5 21:32
 */
public class Client {

    private SocketChannel socketChannel = null;

    private Selector selector = null;

    private static final String IP = "127.0.0.1";

    private static final int PORT = 8080;

    public Client() {
        try {
            socketChannel = SocketChannel.open(new InetSocketAddress(IP, PORT));
            selector = Selector.open();
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
            System.out.println("address: " + socketChannel.getLocalAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 向服务器发送消息
     * @param message
     */
    public void send(String message) {
        try {
            ByteBuffer byteBuffer = ByteBuffer.wrap(message.getBytes());
            socketChannel.write(byteBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 接收从服务器来的消息
     */
    public void receive() {
        try {
            while (true) {
                if (selector.selectNow() != 0) {
                    Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                    while (keyIterator.hasNext()) {
                        SelectionKey selectionKey = keyIterator.next();
                        if (selectionKey.isReadable()) {
                            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                            int length = socketChannel.read(byteBuffer);
                            String message = new String(byteBuffer.array(), 0, length, StandardCharsets.UTF_8);
                            System.out.println(message);
                        }
                        keyIterator.remove();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        // 每隔三秒就查看服务器是否有消息
        new Thread(() -> {
            while (true) {
                client.receive();
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String message = scanner.nextLine();
            client.send(message);
        }
    }
}
