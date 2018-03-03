package test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Test {
	
	public static void main(String[] args) throws InterruptedException {
		
		ParkingCounter parkingCounter = new ParkingCounter();
		ExecutorService executor = Executors.newFixedThreadPool(5);
		((ThreadPoolExecutor)executor).prestartAllCoreThreads();
		executor.execute(new Task(parkingCounter));
		executor.execute(new Task(parkingCounter));
		executor.execute(new Task(parkingCounter));
		executor.execute(new Task(parkingCounter));
		executor.execute(new Task(parkingCounter));
		executor.shutdown();
		executor.awaitTermination(1,TimeUnit.HOURS);
		System.out.println(parkingCounter);
	}
	
	private static class Task implements Runnable {
		private ParkingCounter parkingCounter;
		
		public Task(ParkingCounter parkingCounter) {
			this.parkingCounter = parkingCounter;
		}
		
		@Override
		public void run() {
			for(int i=0;i<500;i++) {
				if(Math.random()<0.5) {
					parkingCounter.carIn();
				} else {
					parkingCounter.carOut();
				}
			}
		}
	}
	private static class ParkingCounter extends AtomicInteger {
		private static final int MAX = 5; //最大停车数
		
		public ParkingCounter() {
			
		}
		
		public boolean carIn() {
			
			while(true) {
				int value = get();
				if(value==MAX) {
					System.out.println("the parking is max");
					return false;
				} else {
					int newValue = value+1;
					if(super.compareAndSet(value,newValue)) {
						System.out.println("car in:"+newValue);
						return true;
					}
				}
			}
		}
		
		public boolean carOut() {
			
			while(true) {
				int value = get();
				if(value==0) {
					System.out.println("the parking is empty");
					return false;
				} else {
					int newValue = value-1;
					if(super.compareAndSet(value,newValue)) {
						System.out.println("car out:"+newValue);
						return true;
					}
				}
			}
		}
	}
}