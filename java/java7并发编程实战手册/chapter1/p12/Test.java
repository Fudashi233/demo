package chapter1.p12;

import java.util.concurrent.TimeUnit;

public class Test {
	
	public static void main(String[] args) {
		
		Task task = new Task();
		ThreadGroup group = new MyThreadGroup("group");
		for(int i=0;i<3;i++) {
			new Thread(group,task).start();
		}
	}
}

class Task implements Runnable {
	
	@Override
	public void run() {
		
		try {
			while(true) {
				int i = 1/(int)(Math.random()*5);
				TimeUnit.MILLISECONDS.sleep(200);
			}
		} catch (InterruptedException e) {
			throw new RuntimeException(Thread.currentThread().getName()+" has been interrupted",e);
		}
	}
}

class MyThreadGroup extends ThreadGroup {

	public MyThreadGroup(String name) {
		
		super(name);
	}
	
	@Override
	public void uncaughtException(Thread t,Throwable e) {
		
		System.out.println("thread:"+t);
		System.out.println("throwable:"+e);
		super.interrupt();
	}
	
}