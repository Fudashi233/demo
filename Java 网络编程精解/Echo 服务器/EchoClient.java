package cn.edu.jxau.socket.echo;

import java.io.*;
import java.net.Socket;
import java.util.Objects;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2019/2/17
 * Time:下午2:30
 */
public class EchoClient {

    private String host;
    private int port;
    private Socket socket;

    public EchoClient(String host, int port) throws IOException {

        this.host = host;
        this.port = port;
        this.socket = new Socket(host, port);
        System.out.println("客户端启动...");
    }

    public static void main(String[] args) throws IOException {
        new EchoClient("127.0.0.1", 8888).talk();
    }

    private PrintWriter getWriter() throws IOException {
        return new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    private BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void talk() throws IOException {

        PrintWriter writer = null;
        BufferedReader reader = null;
        BufferedReader systemIn = null;
        try {
            writer = getWriter();
            reader = getReader();
            systemIn = new BufferedReader(new InputStreamReader(System.in));
            doTalk(writer, reader, systemIn);
        } finally {
            try {
                if (Objects.nonNull(systemIn)) {
                    systemIn.close();
                }
            } finally {
                try {
                    if (Objects.nonNull(reader)) {
                        reader.close();
                    }
                } finally {
                    try {
                        if (Objects.nonNull(writer)) {
                            writer.close();
                        }
                    } finally {
                        if (Objects.nonNull(socket)) {
                            socket.close();
                        }
                    }

                }
            }
        }
    }

    public void doTalk(PrintWriter writer, BufferedReader reader, BufferedReader systemIn) throws IOException {

        String msg = null;
        while (Objects.nonNull(msg = systemIn.readLine())) {
            writer.println(msg);
            writer.flush();
            String reply = reader.readLine();
            System.out.println(reply);
            if (Objects.equals(reply, "bye")) {
                break;
            }
        }
    }
}
