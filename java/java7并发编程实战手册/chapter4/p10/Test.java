package chapter4.p10;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import test.ThreadUtil;

public class Test {
	
	public static void main(String[] args) {
		
		ExecutorService executor = Executors.newCachedThreadPool();
		MyFutureTask[] futureTaskArr = new MyFutureTask[5];
		for(int i=0;i<futureTaskArr.length;i++) {
			futureTaskArr[i] = new MyFutureTask(new Task("task-"+i));
			executor.submit(futureTaskArr[i]);
		}
		executor.shutdown();
		try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			throw new RuntimeException("the "+Thread.currentThread().getName()+" has been interrupted",e);
		}
		/* 取消全部的future task */
		for(int i=0;i<futureTaskArr.length;i++) {
			futureTaskArr[i].cancel(true);
		}
		for(int i=0;i<futureTaskArr.length;i++) {
			if(!futureTaskArr[i].isCancelled()) {
				try {
					System.out.println(futureTaskArr[i].get());
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

class Task implements Callable<String> {
	
	private String name;
	
	public Task(String name) {
		this.name = name;
	}
	
	public String call() {
		
		try {
			ThreadUtil.randomSleep(10000);
		} catch (InterruptedException e) {
			throw new RuntimeException("the "+Thread.currentThread().getName()+" has been interrupted",e);
		}
		return "hello,"+name;
	}
	
	public String getName() {
		return name;
	}
}

class MyFutureTask extends FutureTask<String> {
	
	private String name;
	
	public MyFutureTask(Task task) {
		
		super(task);
		this.name = task.getName();
	}
	
	@Override
	public void done() {
		
		if(super.isCancelled()) {
			System.out.printf("%s has been canceled\n",name);
		} else {
			System.out.printf("%s has been finished\n",name);
		}
	}
}