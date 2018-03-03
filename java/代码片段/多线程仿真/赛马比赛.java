package test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author 付大石
 * 
 */
public class Test {
	
	
	public static void main(String[] args) {
		
		/* 构造fence字符串 */
		StringBuilder fenceBuilder = new StringBuilder();
		for(int i=0;i<PHASE;i++) {
			fenceBuilder.append("**");
		}
		String fence = fenceBuilder.toString(); //模拟围栏
		
		List<Horse> horseList = new ArrayList<>();
		ExecutorService executor = Executors.newCachedThreadPool();
		CyclicBarrier barrier = new CyclicBarrier(N_HORSE,new Runnable() {
			
			@Override
			public void run() { //每完成一个阶段即运行
				System.out.println(fence);
				boolean isFinish = false;
				Horse horse = null;
				Horse winHorse = null;
				for(int i=0;i<horseList.size();i++) {
					horse = horseList.get(i);
					System.out.println(horse.trace());
					if(horse.isFinish()) {
						isFinish = true;
						winHorse = horse;
					}
				}
				System.out.println(fence);
				if(isFinish) {
					System.out.printf("the %s is win\n",winHorse);
					executor.shutdownNow();
				} else {
					System.out.println();
				}
				try {
					System.out.println(ThreadUtil.curThreadName());
					Thread.sleep(200);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					e.printStackTrace();
				}
			}
		});
		for(int i=0;i<N_HORSE;i++) {
			Horse horse = new Horse(String.valueOf(i),barrier);
			horseList.add(horse);
			executor.execute(horse);
		}
	}
	
	private static final int N_HORSE = 3;
	private static final int PHASE = 20;
	private static final int FINISH = 10;
	
	private static class Horse implements Runnable {
		
		private String name;
		private CyclicBarrier barrier;
		private int stride;
		public Horse(String name,CyclicBarrier barrier) {
				
			this.name = name;
			this.barrier = barrier;
		}
		
		@Override
		public void run() {
			
			try {
				while(!ThreadUtil.curIsInterrupted()) {
					stride += (int)(Math.random()*3);
					barrier.await();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (BrokenBarrierException e) {
				e.printStackTrace();
			}
			System.out.println(ThreadUtil.curThreadName()+" is over");
		}
		
		public String trace() {
			
			StringBuilder trace = new StringBuilder();
			for(int i=0;i<stride;i++) {
				trace.append("*");
			}
			trace.append("|");
			return trace.toString();
		}
		
		public boolean isFinish() {
			return stride>=FINISH;
		}
		
		@Override
		public String toString() {
			return String.format("[Horse:name=%s]",name);
		}
	}
}