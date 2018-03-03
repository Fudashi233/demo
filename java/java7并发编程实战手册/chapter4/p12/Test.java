package chapter4.p12;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import test.ThreadUtil;

public class Test {
	
//	public static void main(String[] args) {
//		
//		Runnable r1 = new Task();
//		Runnable r2 = new Task();
//		Runnable r3 = new Task();
//		Runnable r4 = new Task();
//		Runnable r5 = new Task();
//		ExecutorService executor = Executors.newCachedThreadPool();
//		((ThreadPoolExecutor) executor).setRejectedExecutionHandler(new MyRejectedExecutionHandler());
//		executor.execute(r1);
//		executor.execute(r2);
//		executor.execute(r3);
//		executor.shutdown();
//		executor.shutdown();
//		executor.execute(r4);
//		executor.execute(r5);
//	}
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		
		ExecutorService executor = Executors.newCachedThreadPool();
		Future<?> f = executor.submit(new Task());
		System.out.println(f.get()); //null
	}
}

class MyRejectedExecutionHandler implements RejectedExecutionHandler {

	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
		
		System.out.printf("the %s has been rejected\n",r);
		System.out.printf("executor:%s\n",executor);
		System.out.println(executor.isTerminating());
		System.out.println(executor.isTerminated());
	}
}

class Task implements Runnable {

	@Override
	public void run() {
	
		System.out.printf("the %s started\n",Thread.currentThread().getName());
		try {
			ThreadUtil.randomSleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.printf("the %s ended\n",Thread.currentThread().getName());
	}
}