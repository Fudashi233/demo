package chapter7.p02;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import test.ThreadUtil;

public class Test {
	
	public static void main(String[] args) {
		
		ExecutorService executor = new MyExecutor(1,1,60L,TimeUnit.SECONDS,
			new LinkedBlockingQueue<Runnable>());
		executor.execute(new Task());
		executor.execute(new Task());
		executor.execute(new Task());
		executor.execute(new Task());
		executor.execute(new Task());
		executor.shutdown();
		try {
			executor.awaitTermination(1L,TimeUnit.HOURS);
		} catch (InterruptedException e) {
			throw new RuntimeException("the "+ThreadUtil.currentThreadName()+" has been interrupted",e);
		}
	}
}

class MyExecutor extends ThreadPoolExecutor {
	private Map<String,Date> timeLog;
	public MyExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue) {
		
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
		timeLog = new ConcurrentHashMap<>();
	}
	
	@Override
	
	public void shutdown() {
		super.shutdown();
		System.out.println("↓--------------------");
		System.out.printf("going to shutdown\n");
		System.out.printf("completed task count:%d\n",super.getCompletedTaskCount());
		System.out.printf("active task count:%d\n",super.getActiveCount());
		System.out.printf("pending task count:%d\n",super.getQueue().size());
		System.out.println("↑--------------------");
	}
	
	@Override
	public List<Runnable> shutdownNow() {
		
		System.out.println("↓---------------------");
		System.out.printf("going to showdown immediately\n");
		System.out.printf("completed task count:%d\n",super.getCompletedTaskCount());
		System.out.printf("active task count:%d\n",super.getActiveCount());
		System.out.printf("pending task count:%d\n",super.getQueue().size());
		System.out.println("↑---------------------");
		return super.shutdownNow();
	}
	
	@Override
	protected void beforeExecute(Thread thread,Runnable runnable) {
		timeLog.put(String.valueOf(runnable.hashCode()),new Date());
	}
	
	@Override
	protected void afterExecute(Runnable runnable,Throwable throwable) {
		
		System.out.println("↓------------------------");
		System.out.printf("a task is finishing:%s",runnable.toString());
		Date now = new Date();
		Date startTime = timeLog.remove(String.valueOf(runnable.hashCode()));
		float seconds = (now.getTime()-startTime.getTime()) / 1000.0F; //将毫秒转为秒
		System.out.printf("spending time:%f\n",seconds);
		System.out.println("↑------------------------");
	}
}

class Task implements Runnable {
	
	@Override
	public void run() {
		
		try {
			ThreadUtil.randomSleep(5000);
		} catch (InterruptedException e) {
			throw new RuntimeException("the "+ThreadUtil.currentThreadName()+" has been interrupted",e);
		}
	}
}