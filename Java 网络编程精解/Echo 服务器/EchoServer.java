package cn.edu.jxau.socket.echo;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2019/2/17
 * Time:下午2:30
 */
public class EchoServer {

    private int port;
    private ServerSocket serverSocket;

    public EchoServer(int port) throws IOException {

        this.port = port;
        this.serverSocket = new ServerSocket(port);
        System.out.println("服务端启动...");
    }

    public static void main(String[] args) throws IOException {
        new EchoServer(8888).service();
    }

    public void service() throws IOException {

        PrintWriter writer = null;
        BufferedReader reader = null;
        Socket socket = null;
        try {
            socket = serverSocket.accept();
            System.out.println("New connection accept " + socket);
            writer = getWriter(socket);
            reader = getReader(socket);
            String msg = null;
            while (Objects.nonNull(msg = reader.readLine())) {
                writer.println(reply(msg));
                writer.flush();
                if (Objects.equals(msg, "bye")) {
                    break;
                }
            }
        } finally {
            try {
                if (Objects.nonNull(writer)) {
                    writer.close();
                }
            } finally {
                try {
                    if (Objects.nonNull(reader)) {
                        reader.close();
                    }
                } finally {
                    if (Objects.nonNull(socket)) {
                        socket.close();
                    }
                }
            }
        }
    }

    private String reply(String msg) {

        String reply = "echo " + msg;
        System.out.println("reply:" + reply);
        return reply;
    }

    private PrintWriter getWriter(Socket socket) throws IOException {
        return new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    private BufferedReader getReader(Socket socket) throws IOException {
        return new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }
}
