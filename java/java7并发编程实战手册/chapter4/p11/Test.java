package chapter4.p11;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import test.ThreadUtil;

public class Test {
	
	public static void main(String[] args) {
		
		ExecutorService executor = Executors.newCachedThreadPool();
		CompletionService<String> service = new ExecutorCompletionService<>(executor);
		Runnable reportRequest1 = new ReportRequest(service);
		Runnable reportRequest2 = new ReportRequest(service);
		Runnable reportProcess1 = new ReportProcess(service);
		executor.execute(reportRequest1);
		executor.execute(reportRequest2);
		executor.execute(reportProcess1);
	}
}

class ReportGenerator implements Callable<String> {
	
	private String sender;
	private String info;
	
	public ReportGenerator(String sender,String info) {
		this.sender = sender;
		this.info = info;
	}
	@Override
	public String call() throws Exception {
		
		ThreadUtil.randomSleep(5000);
		return sender+":"+info;
	}
}

class ReportProcess implements Runnable {
	
	private CompletionService<String> service;
	private boolean end;
	
	public ReportProcess(CompletionService<String> service) {
		this.service = service;
	}
	
	public void setEnd(boolean end) {
		this.end = end;
	}
	
	@Override
	public void run() {
		
		while(!end) {
			Future<String> future = service.poll();
			if(future!=null) {
				try {
					System.out.println(future.get());
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("process end");
	}
}

class ReportRequest implements Runnable {
	
	private CompletionService<String> service;
	public ReportRequest(CompletionService<String> service) {
		this.service = service;
	}
	@Override
	public void run() {
		service.submit(new ReportGenerator(Thread.currentThread().getName(),"asdasd"));
	}
}

//
//public class Test {
//
//	public static void main(String args[]) {
//		
//		Task t1 = new Task(7000);
//		Task t2 = new Task(5000);
//		Task t3 = new Task(3000);
//		ExecutorService executor = Executors.newCachedThreadPool();
//		CompletionService<String> completionService = new ExecutorCompletionService<>(executor);
//		executor.execute(t1);
//		executor.execute(t2);
//		executor.execute(t3);
//		try {
//			completionService.take();
//			completionService.take();
//			completionService.take();
//		} catch (InterruptedException e) {
//			throw new RuntimeException("the "+ThreadUtil.currentThreadName()+" has been interrupted");
//		}
//	}
//}
//
//class Task implements Runnable {
//	 
//	private long sleep;
//	
//	public Task(long sleep) {
//		this.sleep = sleep;
//	}
//	
//	public void run() {
//		
//		try {
//			Thread.sleep(sleep);
//		} catch (InterruptedException e) {
//			throw new RuntimeException("the "+ThreadUtil.currentThreadName()+" has been interrupted");
//		}
//		System.out.printf("the %s's sleep time is %d",ThreadUtil.currentThreadName(),sleep);
//	}
//}

