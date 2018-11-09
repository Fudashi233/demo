package cn.edu.jxau.producerconsumer;

import cn.edu.jxau.util.CodeUtils;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2018/11/9
 * Time:上午8:15
 */
public class Producer implements Runnable {

    private Buffer<String> buffer;

    public Producer(Buffer<String> buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {

        for (int i = 0; i < 100; i++) {
            CodeUtils.sleep(100);
            String data = "data" + i;
            buffer.producer(data);
            System.out.println("produce " + data);
        }
    }
}
