package test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author 付大石
 * wax on--->wax off--->wax on--->wax off,不断反复
 */
public class Test {
	
	public static void main(String[] args) throws InterruptedException {
	
		Car car = new Car();
		ExecutorService executor = Executors.newCachedThreadPool();
		executor.execute(new WaxOn(car));
		executor.execute(new WaxOff(car));
		TimeUnit.SECONDS.sleep(5);
		executor.shutdownNow();
	}
	
	private static class Car {
		
		private boolean waxOn = false;
		public Car() {
			
		}
		public synchronized void waxed() {
			
			waxOn = true;
			super.notifyAll();
		}
		public synchronized void buffed() {
			
			waxOn = false;
			super.notifyAll();
		}
		public synchronized void waitForWaxing() throws InterruptedException {
			
			while(waxOn==false) {
				super.wait();
			}
		}
		public synchronized void waitForBuffing() throws InterruptedException {
			
			while(waxOn==true) {
				super.wait();
			}
		}
	}
	
	private static class WaxOn implements Runnable {
		
		private Car car;
		
		public WaxOn(Car car) {
			this.car = car;
		}
		
		@Override
		public void run() {
			try {
				while(!ThreadUtil.currIsInterrupted()) {
					System.out.println("wax on");
					TimeUnit.MILLISECONDS.sleep(200);
					car.waxed();
					car.waitForBuffing(); //等待wax off
				}
			} catch (InterruptedException e) {
				throw new RuntimeException("the "+ThreadUtil.currentThreadName()+" has been interrupted");
			}
			System.out.println("finish wax on");
		}
	}
	
	private static class WaxOff implements Runnable {
		
		private Car car;
		
		public WaxOff(Car car) {
			this.car = car;
		}
		
		@Override
		public void run() {
			
			try {
				while(!ThreadUtil.currIsInterrupted()) {
					car.waitForWaxing();// 等待wax on
					System.out.println("wax off");
					TimeUnit.MILLISECONDS.sleep(200);
					car.buffed();
				}
			} catch (InterruptedException e) {
				throw new RuntimeException("the "+ThreadUtil.currentThreadName()+" has been interrutped");
			}
			System.out.println("finish wax off");
		}
	}
}