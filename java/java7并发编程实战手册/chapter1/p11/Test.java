package chapter1.p11;

import java.util.concurrent.TimeUnit;

public class Test {
	
	public static void main(String[] args) {
		
		ThreadGroup group = new ThreadGroup("taskGroup");
		Result result = new Result();
		Runnable task = new Task(result);
		for(int i=0;i<10;i++) {
			new Thread(group,task).start();
		}
		
		waitFinish(group);
		group.interrupt();
	}
	
	private static void waitFinish(ThreadGroup group) {
		try {
			while(group.activeCount()>9) { //仍在执行任务
				TimeUnit.SECONDS.sleep(1);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		group.list();
	}
}

class Task implements Runnable {
	
	private Result result;
	
	public Task(Result result) {
		this.result = result;
	}
	@Override
	public void run() {
		
//		System.out.println(Thread.currentThread().getName()+" start");
		try {
			doTask();
			result.setResult(Thread.currentThread().getName());
		} catch (InterruptedException e) {
			System.out.println(Thread.currentThread().getName()+" has been interrupted");
			return ;
		}
		System.out.println(Thread.currentThread().getName()+" end");
	}
	
	private void doTask() throws InterruptedException {
		
		int random = (int)(Math.random()*50);
		System.out.println(Thread.currentThread().getName()+" random value:"+random);
		TimeUnit.SECONDS.sleep(random);
	}
}

class Result {
	
	private String result;
	public Result() {
		
	}
	public String getString() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
}