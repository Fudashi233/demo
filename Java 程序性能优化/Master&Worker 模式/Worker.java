package cn.edu.jxau.masterworker;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2018/11/6
 * Time:上午8:24
 */
public abstract class Worker<T, E> implements Runnable {


    private Queue<T> resultQueue;
    private Queue<Object> paramQueue = new ConcurrentLinkedQueue<>();

    public Worker() {

    }

    protected abstract T handle(Object param);


    protected abstract E merge();

    @Override
    public void run() {

        while (true) {
            Object param = paramQueue.poll();
            if (Objects.isNull(param)) {
                break;
            }
            T result = handle(param);
            resultQueue.add(result); //收集计算结果
        }
    }

    void submit(Object param) {
        paramQueue.add(param);
    }

    protected Queue<T> getResultQueue() {
        return resultQueue;
    }

    void setResultQueue(Queue<T> resultQueue) {
        this.resultQueue = resultQueue;
    }
}
