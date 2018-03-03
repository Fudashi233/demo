package chapter6.p06;

import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import test.ThreadUtil;

public class Test {
	
	public static void main(String[] args) {
		ConcurrentNavigableMap<Integer,Integer> map = new ConcurrentSkipListMap<>();
		ExecutorService executor = Executors.newCachedThreadPool();
		executor.execute(new Task(map));
		executor.shutdown();
		try {
			executor.awaitTermination(1,TimeUnit.HOURS);
		} catch (InterruptedException e) {
			throw new RuntimeException("the "+ThreadUtil.currentThreadName()+" has been interrupted");
		}
		System.out.println(map.get(1));
		System.out.println(map.get(2));
		System.out.println(map.get(600));
	}
}

class Task implements Runnable {
	
	private ConcurrentNavigableMap<Integer,Integer> map;
	
	public Task(ConcurrentNavigableMap<Integer,Integer> map) {
		this.map = map;
	}
	
	@Override
	public void run() {
		
		for(int i=0;i<500;i++) {
			map.put(i,i);
		}
	}
}