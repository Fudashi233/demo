package cn.edu.jxau.socket.blockserver;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2019/3/18
 * Time:下午7:32
 */
public class EchoServer {

    private static final int PORT = 8899;
    private ServerSocketChannel serverSocketChannel = null;
    private ExecutorService executorService = null;

    public EchoServer() throws IOException {

        executorService = Executors.newCachedThreadPool();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(PORT));
        System.out.println("服务器启动...");
    }

    public static void main(String[] args) throws IOException {
        new EchoServer().service();
    }

    public void service() throws IOException {

        while (true) {
            SocketChannel socketChannel = serverSocketChannel.accept();
            executorService.execute(new Handler(socketChannel));
        }
    }

    private class Handler implements Runnable {

        private SocketChannel socketChannel;

        public Handler(SocketChannel socketChannel) {
            this.socketChannel = socketChannel;
        }

        @Override
        public void run() {

            try {
                Socket socket = socketChannel.socket();
                System.out.println("接收到客户链接：" + socket);
                BufferedReader reader = getBufferedReader(socket);
                String line = null;
                while (Objects.nonNull(line = reader.readLine())) {
                    System.out.println("from client:" + line);
                    if (Objects.equals(line, "bye")) {
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (Objects.nonNull(socketChannel)) {
                        socketChannel.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private PrintWriter getPrintWriter(Socket socket) throws IOException {
            return new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        }

        private BufferedReader getBufferedReader(Socket socket) throws IOException {
            return new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
    }
}