package cn.edu.jxau.masterworker;

import java.util.Queue;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2018/11/7
 * Time:上午8:19
 */
public class PlusWorker extends Worker<Integer, Long> {

    @Override
    protected Integer handle(Object param) {

        Integer integer = (Integer) param;
        return integer * integer * integer;
    }

    @Override
    protected Long merge() {

        Queue<Integer> resultQueue = super.getResultQueue();
        long sum = 0;
        for (Integer result : resultQueue) {
            sum += result;
        }
        return sum;
    }
}
