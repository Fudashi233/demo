package cn.ed.jxau.nio;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;

public class Test {
    
    public static void main(String[] args) throws IOException {
        
        // 使用FileOutputStream获取Channel //
        FileChannel channel = new FileOutputStream("data.txt").getChannel();
        channel.write(ByteBuffer.wrap("1.some text\n".getBytes()));
        channel.close();
        
        // 使用RandomAccessFile获取Channel //
        channel = new RandomAccessFile("data.txt","rw").getChannel();
        channel.position(channel.size());
        channel.write(ByteBuffer.wrap("2.some text".getBytes()));
        channel.close();
        
        // 使用FileInpuStream获取Channel //
        channel = new FileInputStream("data.txt").getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        channel.read(buffer);
        buffer.flip();
        while(buffer.hasRemaining()) {
            System.out.print((char)buffer.get());
        }
        
        // Channels工具类 //
        System.out.println();
        channel = (FileChannel) Channels.newChannel(new FileInputStream("data.txt"));
        buffer = ByteBuffer.allocate(1024);
        channel.read(buffer);
        buffer.flip();
        while(buffer.hasRemaining()) {
            System.out.print((char)buffer.get());
        }
        channel.close();
        
    }
}
