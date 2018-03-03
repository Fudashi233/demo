package chapter6.p04;

import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;


public class Test {
	
	public static void main(String[] args) {
		
		BlockingQueue<Integer> queue = new PriorityBlockingQueue<>(40);
		ExecutorService executor = Executors.newCachedThreadPool();
		executor.execute(new Task());
		executor.execute(new Task());
		executor.execute(new Task());
		executor.execute(new Task());
		executor.shutdown();
		try {
			executor.awaitTermination(1,TimeUnit.HOURS);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		for(int i=0;i<10;i++) {
			try {
				System.out.println(queue.take());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

class Task implements Runnable {

	@Override
	public void run() {
		
	}
	
}
