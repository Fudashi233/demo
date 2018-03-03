package chapter6.p03;

import java.util.Deque;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

import test.ThreadUtil;

public class Test {
	
	public static void main(String[] args) {
		
		BlockingDeque<Integer> buffer = new LinkedBlockingDeque<>();
		ExecutorService executor = Executors.newCachedThreadPool();
		executor.execute(new Buffer(buffer));
		executor.shutdown();
		for(int i=0;i<10;i++) {
			try {
				System.out.println(buffer.take());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
}

class Buffer implements Runnable {
	
	private BlockingDeque<Integer> buffer;
	
	public Buffer(BlockingDeque<Integer> buffer) {
		this.buffer = buffer;
	}
	
	@Override
	public void run() {
		
		for(int i=0;i<10;i++) {
			try {
				ThreadUtil.randomSleep(5000);
			} catch (InterruptedException e) {
				throw new RuntimeException("the "+ThreadUtil.currentThreadName()+" has been interrupted",e);
			}
			try {
				buffer.put(i);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
