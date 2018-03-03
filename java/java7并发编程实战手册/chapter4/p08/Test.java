package chapter4.p08;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Test {
	
	public static void main(String[] args) {
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		((ScheduledThreadPoolExecutor)executor).setContinueExistingPeriodicTasksAfterShutdownPolicy(true);
		executor.scheduleAtFixedRate(new Task(),5L,1L,TimeUnit.SECONDS);
//		try {
//			executor.awaitTermination(10,TimeUnit.SECONDS);
//		} catch (InterruptedException e) {
//			throw new RuntimeException("the "+Thread.currentThread().getName()+" has been interrupted",e);
//		}
		executor.shutdown();
		System.out.println("FINISH");
	}
	
//	public static void main(String[] args) {
//		
//		// Create a ScheduledThreadPoolExecutor
//		ScheduledExecutorService executor=Executors.newScheduledThreadPool(1);
//		System.out.printf("Main: Starting at: %s\n",new Date());
//
//		// Create a new task and sends it to the executor. It will start with a delay of 1 second and then
//		// it will execute every two seconds
//		Task task=new Task();
//		ScheduledFuture<?> result=executor.scheduleAtFixedRate(task, 1, 2, TimeUnit.SECONDS);
//		
//		// Controlling the execution of tasks
//		for (int i=0; i<10; i++){
//			System.out.printf("Main: Delay: %d\n",result.getDelay(TimeUnit.MILLISECONDS));
//			try {
//				TimeUnit.MILLISECONDS.sleep(500);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//		
//		// Finish the executor
//		executor.shutdown();
//		System.out.printf("Main: No more tasks at: %s\n",new Date());
//		// Verify that the periodic tasks no is in execution after the executor shutdown()
//		try {
//			TimeUnit.SECONDS.sleep(5);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		// The example finish
//		System.out.printf("Main: Finished at: %s\n",new Date());
//		
//	}
}
class Task implements Runnable {
	
	public void run() {
		
		System.out.printf("the %s started\n",Thread.currentThread().getName());
	}
}