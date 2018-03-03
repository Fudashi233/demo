package chapter4.p02;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import test.ThreadUtil;

public class Test {
	
	public static void main(String[] args) throws InterruptedException {
		
		Server server = new Server();
		for(int i=0;i<10;i++) {
			server.execute(new Request(""+i));
		}
		server.shutdown();
		System.out.println("-f-i-n-i-s-h-");
	}
}

class Server {
	
	private ThreadPoolExecutor executor;
	public Server() {
		
		executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
		executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
	}
	
	public void execute(Request request) {
	
		executor.execute(request);
		System.out.printf("poolSize:%d,activeCount:%d,completedTaskCount:%d\n",executor.getPoolSize(),
				executor.getActiveCount(),executor.getCompletedTaskCount());
	}
	
	/**
	 * 关闭服务器
	 */
	public void shutdown() {
		
		executor.shutdown();
	}
}

class Request implements Runnable {
	
	private String requestName;
	public Request(String requestName) {
		
		this.requestName = requestName;
	}
	@Override
	public void run() {
		
		try {
			System.out.printf("%s start\n",requestName);
			ThreadUtil.randomSleep(5000);
		} catch (InterruptedException e) {
			throw new RuntimeException("the "+Thread.currentThread().getName()+" has been interrupted",e);
		}
		System.out.printf("%s end\n",requestName);
	}
}