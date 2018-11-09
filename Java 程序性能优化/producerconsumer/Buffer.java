package cn.edu.jxau.producerconsumer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2018/11/9
 * Time:上午8:15
 */
public class Buffer<T> {

    /**
     * 缓冲区
     */
    private BlockingQueue<T> buffer = new ArrayBlockingQueue<T>(5);

    public void producer(T data) {
        try {
            buffer.put(data);
        } catch (InterruptedException e) {
            throw new RuntimeException(Thread.currentThread().getName() + " 被中断", e);
        }
    }

    public T consumer() {
        try {
            return buffer.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(Thread.currentThread().getName() + " 被中断", e);
        }
    }
}
