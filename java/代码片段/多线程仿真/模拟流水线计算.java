package test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;


/**
 * @author 付大石
 * 
 * 模拟流水线的工作方式，实现(B+C)*B/2
 * P1:计算r1=B+C,然后将结果送给P2
 * P2:计算r2=r1*B,然后将结果送给P3
 * P3:计算r3=r2/2,然后将结果送给输出
 */
public class Test {
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		
		P1 p1 = new P1();
		P2 p2 = new P2();
		P3 p3 = new P3();
		ExecutorService executor = Executors.newFixedThreadPool(3);
		executor.execute(p1);
		executor.execute(p2);
		executor.execute(p3);
		executor.shutdown();
		for(int i=0;i<100;i++) {
			P1.queue.add(new Message(Math.random()*20,Math.random()*20));
		}
		System.out.println("装填完毕");
		executor.awaitTermination(1,TimeUnit.HOURS);
		System.out.println("工作完毕");
	}
	
	private static class Message {
		
		public double i;
		public double j;
		public Message(double i,double j) {
			this.i = i;
			this.j = j;
		}
	}
	
	private static class P1 implements Runnable {
		
		public static BlockingQueue<Message> queue;
		
		public P1() {
			queue = new LinkedBlockingQueue<Message>();
		}
		
		@Override
		public void run() {
			
			Message message = null;
			try {
				while((message=queue.poll(5,TimeUnit.SECONDS)) != null) {
					message.j = message.i*message.j;
					P2.queue.put(message);
				}
			} catch (InterruptedException e) {
				throw new RuntimeException("the "+ThreadUtil.currentThreadName()+" has been interrupted",e);
			}
		}
	}
	
	private static class P2 implements Runnable {
		
		public static BlockingQueue<Message> queue;
		
		public P2() {
			queue = new LinkedBlockingQueue<Message>();
		}
		@Override
		public void run() {
			
			Message message = null;
			try {
				while((message=queue.poll(5,TimeUnit.SECONDS)) != null) {
					message.i = message.j*message.i;
					P3.queue.put(message);
				}
			} catch (InterruptedException e) {
				throw new RuntimeException("the "+ThreadUtil.currentThreadName()+" has been interrupted",e);
			}
		}
	}
	
	private static class P3 implements Runnable {
		
		
		public static BlockingQueue<Message> queue;
		
		public P3() {
			queue = new LinkedBlockingQueue<Message>();
		}
		
		@Override
		public void run() {
			
			Message message = null;
			try {
				while((message=queue.poll(5,TimeUnit.SECONDS)) != null) {
					System.out.println(message.i/2);
				}
			} catch (InterruptedException e) {
				throw new RuntimeException("the "+ThreadUtil.currentThreadName()+" has been interrupted",e);
			}
		}
	}
}