package chapter4.p09;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Test {
	
	public static void main(String[] args) {
		
		ExecutorService executor = Executors.newCachedThreadPool();
		Future<String> future = executor.submit(new Task());
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			throw new RuntimeException("the "+Thread.currentThread().getName()+" has been interrupted",e);
		}
		System.out.println(future.isCancelled()+"	"+future.isDone());
		future.cancel(true);
		System.out.println(future.isCancelled()+"	"+future.isDone());
		try {
			System.out.println(future.get());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
}

class Task implements Callable<String> {
	
	public String call() throws InterruptedException {
		
		while(true) {
			System.out.printf("the %s\n",Thread.currentThread().getName());
			TimeUnit.MILLISECONDS.sleep(200);
		}
	}
}