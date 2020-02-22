package org.augustus.bio;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author LinYongJin
 * @date 2020/2/22 14:56
 */
public class BIOServer {

    public static void main(String[] args) throws IOException {
        // 创建ServerSocket
        ServerSocket serverSocket = new ServerSocket(6666);
        // 创建一个线程池
        ExecutorService threadPool = Executors.newCachedThreadPool();
        while (true) {
            System.out.println("等待连接");
            final Socket socket = serverSocket.accept();
            System.out.println("接收到连接");
            // 开启一个线程处理连接
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("id: " + Thread.currentThread().getId() + ", name: " + Thread.currentThread().getName());
                    handler(socket);
                }
            });
        }
    }

    public static void handler(Socket socket) {
        try {
            byte[] data = new byte[1024];
            InputStream inputStream = socket.getInputStream();
            int length;
            StringBuffer sb = new StringBuffer();
            System.out.println("等待读取");
            while ((length = inputStream.read(data)) != -1) {
                sb.append(new String(data, 0, length, StandardCharsets.UTF_8));
                System.out.println("来自客户端的消息" + sb);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                    System.out.println("关闭连接");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
