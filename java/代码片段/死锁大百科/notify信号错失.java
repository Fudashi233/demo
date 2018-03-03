package test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author 付大石
 * 信号错失造成死锁
 */
public class Test {
	
	public static void main(String[] args) {
		
		Test t = new Test();
//		new Thread() {
//			@Override
//			public void run() {
//				System.out.println("begin t1");
//				try {
//					t.bar();
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//				System.out.println("end t1");
//			}
//		}.start();
		new Thread() {
			@Override
			public void run() {
				System.out.println("begin t2");
				t.foo();
				System.out.println("end t2");
			}
		}.start();
	}
	private void foo() {
		
		synchronized(this) {
			System.out.println("notify");
			super.notify();
		}
	}
	private void bar() throws InterruptedException {
		
		Thread.yield();
		TimeUnit.MILLISECONDS.sleep(300);
		synchronized(this) {
			System.out.println("wait");
			super.wait();
		}
	}
}