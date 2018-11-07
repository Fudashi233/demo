package cn.edu.jxau.masterworker;

import cn.edu.jxau.util.CodeUtils;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2018/11/6
 * Time:上午8:32
 */
public class Master<T, E> {

    private Worker<T, E> worker;
    private List<Thread> threadList;
    private Queue<T> resultQueue = new ConcurrentLinkedQueue<>();

    public Master(Worker<T, E> worker, int workerCount) {
        this.worker = worker;
        this.worker.setResultQueue(resultQueue);
        threadList = new ArrayList<>();
        for (int i = 0; i < workerCount; i++) {
            threadList.add(new Thread(worker));
        }
    }

    public void submit(Object param) {
        worker.submit(param);
    }

    public Queue<T> getResultQueue() {
        return resultQueue;
    }

    public void execute() {
        for (Thread thread : threadList) {
            thread.start();
        }
    }

    public E getMergeResult() {
       return worker.merge();
    }

    public boolean isComplete() {

        for (Thread thread : threadList) {
            if (!Objects.equals(thread.getState(), Thread.State.TERMINATED)) {
                return false;
            }
        }
        return true;
    }
}

