package test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Test {
	
	public static void main(String[] args) throws InterruptedException {
		
		Object obj = new Object();
		Task t1 = new Task(obj);
		Task t2 = new Task(obj);
		t1.start();
		t2.start();
		Thread.sleep(5000);
		t1.suspendX();
		Thread.sleep(5000);
		t1.resumeX();
	}
	
	private static class Task extends Thread {
		
		private Object obj;
		private boolean suspend;
		public Task(Object obj) {
			this.obj = obj;
		}
		
		@Override
		public void run() {
			
			while(true) {
				while(suspend) {
					synchronized(this) {
						try {
							wait();
						} catch (InterruptedException e) {
							throw new RuntimeException(e);
						}
					}
				}
				synchronized(obj) {
					System.out.println(ThreadUtil.currentThreadName());
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
		
		public void suspendX() {
			suspend = true;
		}
		
		public void resumeX() {
			suspend = false;
			synchronized(this) {
				super.notify();
			}
		}
	}
}
