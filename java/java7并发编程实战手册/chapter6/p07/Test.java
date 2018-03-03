package chapter6.p07;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

public class Test {
	
	public static void main(String[] args) {
		
		ExecutorService executor = Executors.newCachedThreadPool();
		executor.execute(new Task());
		executor.execute(new Task());
		executor.execute(new Task());
		executor.shutdown();
	}
}
class Task implements Runnable {

	@Override
	public void run() {
		for(int i=0;i<10;i++) {
			System.out.printf("random:%d\n",ThreadLocalRandom.current().nextInt(10));
		}
	}
}