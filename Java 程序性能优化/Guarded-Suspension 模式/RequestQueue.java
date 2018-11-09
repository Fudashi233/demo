package cn.edu.jxau.guardedsuspension;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2018/11/8
 * Time:上午8:27
 */
public class RequestQueue {

    private Queue<Request> requestList = new LinkedList<>();

    public synchronized Request getRequest() {

        while (requestList.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException("The current thread is interrupted，threadName=" + Thread.currentThread(), e);
            }
        }
        return requestList.remove();
    }

    public synchronized void addRequest(Request request) {
        requestList.add(request);
        notify();
    }
}
