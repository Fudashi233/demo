package test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author 付大石
 * 吐司block queue。
 * 假设有三个任务，一个制作吐司，一个抹黄油，一个抹果酱。
 * 使用BlockQueue完成这个流水线
 */
public class Test {
	
	private enum Status {DRY,BUTTERED,JAMMED};
	
	public static void main(String[] args) throws InterruptedException {
		
		BlockingQueue<Toast> buttered = new LinkedBlockingQueue<>();
		BlockingQueue<Toast> jammed = new LinkedBlockingQueue<>();
		BlockingQueue<Toast> eater = new LinkedBlockingQueue<>();
		ExecutorService executor = Executors.newCachedThreadPool();
		executor.execute(new Toaster(buttered));
		executor.execute(new Buttered(buttered,jammed));
		executor.execute(new Jammed(jammed,eater));
		executor.execute(new Eater(eater));
		executor.shutdown();
		executor.awaitTermination(1,TimeUnit.HOURS);
		System.out.println("all is over");
	}
	
	private static class Toast {
		private Status status = Status.DRY;
		private final int id;
		public Toast(int id) {
			this.id = id;
		}
		public void buttered() {
			status = Status.BUTTERED;
		}
		public void jammed() {
			status = Status.JAMMED;
		}
		public int getID() {
			return id;
		}
		public Status getStatus() {
			return status;
		}
		@Override
		public String toString() {
			return String.format("toast:%d",id);
		}
	}
	
	/**
	 * @author 付大石
	 * 生成toaster
	 */
	private static class Toaster implements Runnable {
		
		private BlockingQueue<Toast> buttered;
		
		private Toaster(BlockingQueue<Toast> buttered) {
			this.buttered = buttered;
		}
		
		@Override
		public void run() {
			
			try {
				for(int i=0;i<20;i++) {
					ThreadUtil.randomSleep(1000);
					buttered.put(new Toast(i)); //构造Toast对象，放入buttered处理队列中
				}
			} catch (InterruptedException e) {
				throw new RuntimeException("the "+ThreadUtil.currentThreadName()+" has been interrupted");
			}
		}
	}
	
	/**
	 * @author 付大石
	 * 从buttered队列中取出Toast对象，进行对应的加工操作，
	 * 然后放入jammed队列
	 */
	private static class Buttered implements Runnable {
		
		private BlockingQueue<Toast> buttered;
		private BlockingQueue<Toast> jammed;
		public Buttered(BlockingQueue<Toast> buttered,BlockingQueue<Toast> jammed) {
			
			this.buttered = buttered;
			this.jammed = jammed;
		}
		
		@Override
		public void run() {
			
			Toast t = null;
			try {
				while((t=buttered.poll(5,TimeUnit.SECONDS))!=null) {
					t.buttered();
					jammed.put(t); //放入jammed处理队列中
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("buttered is over");
		}
	}
	
	/**
	 * @author 付大石
	 * 从jammed队列中取出Toast对象并处理，然后放入eater队列中
	 */
	public static class Jammed implements Runnable {
		
		private BlockingQueue<Toast> jammed;
		private BlockingQueue<Toast> eater;
		
		public Jammed(BlockingQueue<Toast> jammed,BlockingQueue<Toast> eater) {
			
			this.jammed = jammed;
			this.eater = eater;
		}
		
		@Override
		public void run() {
			
			Toast t = null;
			try {
				while((t = jammed.poll(5,TimeUnit.SECONDS))!=null) {
					t.jammed();
					eater.put(t); //放入eater等待队列中
				}
			} catch (InterruptedException e) {
				throw new RuntimeException("the "+ThreadUtil.currentThreadName()+" has been interrutped",e);
			}
			System.out.println("jammed is over");
		}
	}
	
	public static class Eater implements Runnable {
		
		private BlockingQueue<Toast> eater;
		public Eater(BlockingQueue<Toast> eater) {
			this.eater = eater;
		}
		
		@Override
		public void run() {
			
			Toast t = null;
			try {
				while((t = eater.poll(5,TimeUnit.SECONDS))!=null) {
					System.out.println(t);
				}
			} catch (InterruptedException e) {
				throw new RuntimeException("the "+ThreadUtil.currentThreadName()+" has been interrupted");
			}
			System.out.println("eater is over");
		}
	}
}