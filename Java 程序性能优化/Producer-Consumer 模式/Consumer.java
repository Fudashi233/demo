package cn.edu.jxau.producerconsumer;

import cn.edu.jxau.util.CodeUtils;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2018/11/9
 * Time:上午8:15
 */
public class Consumer implements Runnable {

    private Buffer<String> buffer;

    public Consumer(Buffer<String> buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {

        while (true) {
            String data = buffer.consumer();
            CodeUtils.sleep(500);
            System.out.println("consume " + data);
        }
    }
}
