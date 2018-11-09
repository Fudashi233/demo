package cn.edu.jxau.producerconsumer;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2018/11/9
 * Time:上午8:31
 */
public class Main {

    public static void main(String[] args) {

        Buffer<String> buffer = new Buffer<>();
        new Thread(new Consumer(buffer)).start();
        new Thread(new Producer(buffer)).start();
    }
}
