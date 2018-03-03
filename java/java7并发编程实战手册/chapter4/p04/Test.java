package chapter4.p04;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import test.ThreadUtil;

public class Test {
	
	public static void main(String[] args) {
		
		ExecutorService executor = Executors.newCachedThreadPool();
		List<Future<Integer>> list = new ArrayList<Future<Integer>>();
		for(int i=0;i<10;i++) {
			int temp = (int)(Math.random()*10);
			Future<Integer> future = executor.submit(new Factorial(temp));
			list.add(future);
		}
		try {
			for(int i=0;i<list.size();i++) {
				System.out.println(i+"	"+list.get(i).get());
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
}

class Factorial implements Callable<Integer> {
	
	private Integer num;
	public Factorial(Integer num) {
		
		this.num = num;
	}
	
	@Override
	public Integer call() throws Exception {
		
		int result = 1;
		if(num<=1) {
			result = 1;
		} else {
			for(int i=2;i<=num;i++) {
				result *= i;
			}
		}
		try {
			ThreadUtil.randomSleep(5000);
		} catch(InterruptedException e) {
			throw new RuntimeException("the "+Thread.currentThread().getName()+" has been interrupted");
		}
		return result;
	}
}