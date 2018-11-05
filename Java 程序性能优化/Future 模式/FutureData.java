package cn.edu.jxau.future;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Data:2018/11/5
 * Time:上午8:21
 */
public class FutureData implements Data {

    private RealData realData;
    private boolean isReady = false;

    public synchronized void setRealData(RealData realData) {

        if (isReady) {
            return;
        }
        this.realData = realData;
        isReady = true;
        notify();
    }

    public synchronized String getResult() {

        while (!isReady) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException("线程被非法中断，" + Thread.currentThread(), e);
            }
        }
        return realData.getResult();
    }
}
