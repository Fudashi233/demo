package cn.edu.jxau.socket.blockserver;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.Objects;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2019/3/18
 * Time:下午8:06
 */
public class EchoClient {

    private static final int PORT = 8899;
    private SocketChannel socketChannel = null;

    public EchoClient(String hostname) throws IOException {

        socketChannel = socketChannel.open();
        socketChannel.socket().connect(new InetSocketAddress(hostname, PORT));
    }

    public static void main(String[] args) throws IOException {
        new EchoClient("127.0.0.1").talk();
    }

    public void talk() throws IOException {

        Socket socket = socketChannel.socket();
        PrintWriter writer = getPrintWriter(socket);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = null;
        while (Objects.nonNull(line = reader.readLine())) {
            writer.println(line);
            if (Objects.equals(line, "bye")) {
                break;
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
