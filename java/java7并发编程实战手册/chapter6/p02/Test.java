package chapter6.p02;

import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import test.ThreadUtil;

public class Test {

	public static void main(String[] args) {
		
		//Deque<Integer> deque = new LinkedList<Integer>();
		Deque<Integer> deque = new ConcurrentLinkedDeque<Integer>();
		ExecutorService executor = Executors.newCachedThreadPool();
		executor.execute(new OfferTask(deque));
		executor.execute(new OfferTask(deque));
		executor.execute(new OfferTask(deque));
		executor.execute(new PollTask(deque));
		executor.execute(new PollTask(deque));
		executor.execute(new PollTask(deque));
		executor.shutdown();
		
		try {
			executor.awaitTermination(1,TimeUnit.HOURS);
		} catch (InterruptedException e) {
			throw new RuntimeException("the "+ThreadUtil.currentThreadName()+" has been interrupted",e);
		}
		System.out.println(deque.size());
	}
	
	/**
	 * 非阻塞式测试
	 */
	private static void foo() {
		
		Deque<Integer> deque = new ConcurrentLinkedDeque<Integer>();
		deque.push(1);
		deque.pop();
		deque.pop(); //java.util.NoSuchElementException
	}
}

class OfferTask<T> implements Runnable {
	
	Deque<Integer> deque;
	
	public OfferTask(Deque<Integer> deque) {
		this.deque = deque;
	}
	
	@Override
	public void run() {
		
		for(int i=0;i<100;i++) {
			deque.add(i);
			Thread.yield();
		}
	}
}

class PollTask<T> implements Runnable {
	
	Deque<Integer> deque;
	
	public PollTask(Deque<Integer> deque) {
		this.deque = deque;
	}
	
	@Override
	public void run() {
		
		for(int i=0;i<50;i++) {
			deque.pollFirst();
			deque.pollLast();
		}
	}
}