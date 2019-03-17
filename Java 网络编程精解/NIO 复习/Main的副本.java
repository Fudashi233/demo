package cn.edu.jxau.nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2019/3/17
 * Time:下午2:29
 */
public class Main {

    public static void main(String[] args) throws IOException {

        test05();
    }

    /**
     * 通过FileOutputStream，RandomAccessFile，FileInputStream拿到通道，并使用缓冲器进行读写
     *
     * @throws IOException
     */
    private static void test01() throws IOException {


        FileChannel fileChannel = new FileOutputStream("/Users/fudashi/a.txt").getChannel();
        fileChannel.write(ByteBuffer.wrap("Hello world!".getBytes()));
        fileChannel.close();

        fileChannel = new RandomAccessFile("/Users/fudashi/a.txt", "rw").getChannel();
        fileChannel.position(fileChannel.size());
        fileChannel.write(ByteBuffer.wrap("Hello world!".getBytes()));
        fileChannel.close();

        fileChannel = new FileInputStream("/Users/fudashi/a.txt").getChannel();
        ByteBuffer byteBuf = ByteBuffer.allocate(1024);
        fileChannel.read(byteBuf);
        byteBuf.flip();
        while (byteBuf.hasRemaining()) {
            System.out.print((char) byteBuf.get());
        }
    }

    /**
     * 利用nio复制文件
     *
     * @throws IOException
     */
    private static void test02() throws IOException {

        FileChannel inChannel = new FileInputStream("/Users/fudashi/a.txt").getChannel();
        FileChannel outChannel = new FileOutputStream("/Users/fudashi/b.txt").getChannel();
        ByteBuffer byteBuf = ByteBuffer.allocate(1024);
        while (inChannel.read(byteBuf) != -1) {
            System.out.println(byteBuf);
            byteBuf.flip();
            outChannel.write(byteBuf);
            byteBuf.clear();
        }
        inChannel.close();
        outChannel.close();
    }

    /**
     * nio与编码，想要不乱码，要么在读出时解码
     */
    private static void test03() throws IOException {

        FileChannel inChannel = new FileInputStream("/Users/fudashi/a.txt").getChannel();
        ByteBuffer byteBuf = ByteBuffer.allocate(1024);
        inChannel.read(byteBuf);
        byteBuf.flip();
        System.out.println(byteBuf.asCharBuffer()); //乱码
        byteBuf.rewind();
        String encoding = System.getProperty("file.encoding");
        System.out.println(encoding + "   " + Charset.forName(encoding).decode(byteBuf)); //在读出时解码
    }

    /**
     * 想要不乱码，要么在写入时编码
     */
    private static void test04() throws IOException {

        // 想要不乱码，要么在写入时编码
        FileChannel outChannel = new FileOutputStream("/Users/fudashi/a.txt").getChannel();
        outChannel.write(Charset.forName("UTF-16BE").encode("你好 text")); //为什么是UTF-16BE
        outChannel.close();
        FileChannel inChannel = new FileInputStream("/Users/fudashi/a.txt").getChannel();
        ByteBuffer byteBuf = ByteBuffer.allocate(1024);
        inChannel.read(byteBuf);
        byteBuf.flip();
        System.out.println(byteBuf.asCharBuffer());
    }

    private static void test05() throws IOException {

        // 想要不乱码，要么在写入时编码
        FileChannel outChannel = new FileOutputStream("/Users/fudashi/a.txt").getChannel();
        ByteBuffer byteBuf = ByteBuffer.allocate(1024);
        byteBuf.asCharBuffer().put("你好 text");
        outChannel.write(byteBuf); //为什么是UTF-16BE
        outChannel.close();
        FileChannel inChannel = new FileInputStream("/Users/fudashi/a.txt").getChannel();
        byteBuf = ByteBuffer.allocate(1024);
        inChannel.read(byteBuf);
        byteBuf.flip();
        System.out.println(byteBuf.asCharBuffer());
    }
}
