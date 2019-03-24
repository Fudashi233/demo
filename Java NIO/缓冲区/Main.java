package cn.edu.jxau.nio.buffer;

import com.google.common.collect.Lists;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2019/3/24
 * Time:下午5:53
 */
public class Main {


    public static void main(String[] args) {

        List<String> list = Lists.newArrayList();
        //boolean flag = Optional.ofNullable(list).orElse(Lists.newArrayList()).stream().filter(Objects::nonNull).anyMatch("1"::equals);//false
        //System.out.println(flag);
        boolean flag = Optional.ofNullable(list).orElse(Lists.newArrayList()).stream().filter(Objects::nonNull).allMatch("1"::equals);//true
        System.out.println(flag);
        flag = Optional.ofNullable(list).orElse(Lists.newArrayList()).stream().filter(Objects::nonNull).noneMatch("1"::equals);//true
        System.out.println(flag);
    }

    /**
     * 填充和释放缓冲区 ----------------------------------------------------------------
     */
    private static void test01() {

        String[] stringArr = {"1234567", "abcdefg", "ABCDEFG"};
        CharBuffer charBuf = CharBuffer.allocate(512);
        for (int i = 0; i < stringArr.length; i++) {
            fillBuffer(stringArr[i], charBuf);
            drainBuffer(charBuf);
        }
    }

    private static void fillBuffer(String str, CharBuffer charBuf) {

        charBuf.clear();
        for (int i = 0; i < str.length(); i++) {
            charBuf.put(str.charAt(i));
        }
    }

    private static void drainBuffer(CharBuffer charBuf) {

        charBuf.flip();
        while (charBuf.hasRemaining()) {
            System.out.print(charBuf.get());
        }
        System.out.println();
    }

    /**
     * 缓冲器比较之：equals()
     */
    private static void test02() {

        // 两个缓冲器必须类型相等 //
        ByteBuffer byteBuf1 = ByteBuffer.wrap("hello".getBytes());
        ByteBuffer byteBuf2 = ByteBuffer.wrap("hello".getBytes());
        System.out.println(Objects.equals(byteBuf1.asCharBuffer(), byteBuf2));

        // 在每个缓冲区中应被 Get()函数返回的剩余数据元素序列必须一致 //
        ByteBuffer byteBuf3 = ByteBuffer.wrap("00hello".getBytes());
        byteBuf3.position(2);
        System.out.println(Objects.equals(byteBuf1, byteBuf3));
    }

    /**
     * 缓冲区比较之：compareTo()
     */
    private static void test03() {

        // 两个缓冲器必须类型相等，不同类型之间的比较无法编译 //
        ByteBuffer byteBuf1 = ByteBuffer.wrap("hello".getBytes());
        ByteBuffer byteBuf2 = ByteBuffer.wrap("hello".getBytes());
        //System.out.println(byteBuf1.asCharBuffer().compareTo(byteBuf2)); // complier err，不同类型之间不可比较

        // 比较的是剩余的元素 //
        ByteBuffer byteBuf3 = ByteBuffer.wrap("00hello".getBytes());
        byteBuf3.position(2);
        System.out.println(byteBuf1.compareTo(byteBuf3));
    }
}
