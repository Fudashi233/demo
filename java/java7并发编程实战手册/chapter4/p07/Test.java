package chapter4.p07;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Test {
	
	public static void main(String[] args) {
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		List<ScheduledFuture<String>> list = new ArrayList<>();
		for(int i=0;i<10;i++) {
			list.add(executor.schedule(new Task(),i+1,TimeUnit.SECONDS));
		}
		executor.shutdown();
		try {
			System.out.println(((ThreadPoolExecutor)executor).getCompletedTaskCount());
			while(((ThreadPoolExecutor)(executor)).getCompletedTaskCount()!=10) {
				
				ScheduledFuture<String> scheduledFuture = list.get(0);
				System.out.println(scheduledFuture.getDelay(TimeUnit.MILLISECONDS)+"	"+scheduledFuture.isDone());
				TimeUnit.MILLISECONDS.sleep(500);
			}
			executor.awaitTermination(1,TimeUnit.DAYS);
		} catch (InterruptedException e) {
			throw new RuntimeException("the "+Thread.currentThread().getName()+" has been interrupted",e);
		}
		System.out.println("FINISH");
	}
}
class Task implements Callable<String> {
	
	public String call() {
		
		System.out.printf("the %s started\n",Thread.currentThread().getName());
		return "Hello world";
	}
}
