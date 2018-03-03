package test;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author 付大石
 * 银行出纳员仿真，银行出纳员（Teller）的数量会随着顾客（Customer）的
 * 数量变化
 */
public class Test {
	
	public static void main(String[] args) throws InterruptedException {
	    
	    Object lock = new Object();
	    Thread t1 = new Thread(new WaitTask(lock),"wait1");
	    Thread t2 = new Thread(new WaitTask(lock),"wait2");
	    Thread t3 = new Thread(new NotifyTask(lock),"notify");
	    t1.start();
	    t2.start();
	    Thread.sleep(2000);
	    t3.start();
	}
	
	public static class WaitTask implements Runnable {
	    
	    private Object lock;
	    public WaitTask(Object lock) {
	        this.lock = lock;
	    }
	    
	    @Override
	    public void run() {
	        
	        synchronized(lock) {
	            System.out.println(ThreadUtil.curThreadName()+" wait");
	            try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
	            System.out.println(ThreadUtil.curThreadName()+" notify");
	        }
	    }
	}
	
	public static class NotifyTask implements Runnable {
	    
	    private Object lock;
	    
	    public NotifyTask(Object lock) {
	        this.lock = lock;
	    }
	    
	    @Override
	    public void run() {
	        
	        synchronized(lock) {
	            lock.notify();
	        }
	    }
	}
}