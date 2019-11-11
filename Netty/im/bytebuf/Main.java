package cn.edu.jxau.im.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2019/11/3
 * Time:下午2:51
 */
public class Main {

    public static void main(String[] args) {

        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(9, 1000);
        //print("init", buf);

        buf.writeBytes(new byte[]{1, 2, 3, 4});
        //print("write byte arr", buf);

        buf.writeInt(1);
        //print("write int", buf);

        buf.writeBytes(new byte[]{1});
        //print("write byte arr", buf);

        buf.writeBytes(new byte[]{1});
        //print("write byte arr", buf);

        //System.out.println(buf.getByte(3));
        //System.out.println(buf.getShort(3));
        //System.out.println(buf.getInt(4));

        byte[] byteArr = new byte[buf.readableBytes()];
        buf.readBytes(byteArr);
        print("readBytes()", buf);
    }

    private static void print(String action, ByteBuf buf) {

        System.out.println("after action：" + action);
        System.out.println("buf.capacity " + buf.capacity());
        System.out.println("buf.maxCapacity " + buf.maxCapacity());

        System.out.println("buf.readerIndex " + buf.readerIndex());
        System.out.println("buf.readableBytes " + buf.readableBytes());
        System.out.println("buf.isReadable " + buf.isReadable());

        System.out.println("buf.writerIndex " + buf.writerIndex());
        System.out.println("buf.writableBytes " + buf.writableBytes());
        System.out.println("buf.isWritable " + buf.isWritable());
        System.out.println("buf.maxWritableBytes " + buf.maxWritableBytes());
    }
}
