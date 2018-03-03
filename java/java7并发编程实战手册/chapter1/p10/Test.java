package chapter1.p10;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Test {

	public static void main(String[] args) {
		
//		Runnable task = new UnsafeTask();
//		try {
//			for(int i=0;i<10;i++) {
//				Thread t = new Thread(task);
//				t.start();
//				TimeUnit.SECONDS.sleep(2);  //使task中的date不同
//			}
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		
		
		Runnable task = new SafeTask();
		try {
			for(int i=0;i<10;i++) {
				Thread t = new Thread(task);
				t.start();
				TimeUnit.SECONDS.sleep(2);  //使task中的date不同
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

class UnsafeTask implements Runnable {
	
	private Date date;
	@Override
	public void run() {
		
		date = new Date();
//		System.out.println("thread:"+Thread.currentThread().getName()+"\tstart:"+date);
		try {
			TimeUnit.SECONDS.sleep((int)Math.rint(Math.random()*10));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
//		System.out.println("thread:"+Thread.currentThread().getName()+"\tend:"+date);
	}
}

class SafeTask implements Runnable {
	
	private ThreadLocal<Date> date = new MyThreadLocal();
	@Override
	public void run() {
		
		System.out.println("thread:"+Thread.currentThread().getName()+"\tstart:"+date.get());
		try {
			TimeUnit.SECONDS.sleep((int)Math.rint(Math.random()*10));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("thread:"+Thread.currentThread().getName()+"\tend:"+date.get());
	}
}

class MyThreadLocal extends ThreadLocal<Date> {
	
	public MyThreadLocal() { //仅会调用一次
		super();
		System.out.println("77---");
	}
	protected Date initialValue(){  //每start一个线程就会执行
		System.out.println("81---");
		return new Date();
	}
}