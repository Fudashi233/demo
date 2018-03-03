package chapter5.p05;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

import test.ThreadUtil;

public class Test {
	
	public static void main(String[] args) {
		
		int[] seed = {1,2,3,4,5,6,7,8,9,10};
		int[] arr = new int[100];
		int count = 0 ;
		int target = 2;
		for(int i=0;i<arr.length;i++) {
			arr[i] = seed[(int)(Math.random()*seed.length)];
			if(arr[i]==target) {
				count++;
			}
		}
		System.out.printf("there have %d targets\n",count);
		RecursiveTask<Integer> task = new Task(arr,target);
		ForkJoinPool pool = new ForkJoinPool();
		pool.execute(task);
		pool.shutdown();
		try {
			pool.awaitTermination(1,TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
//		if(task.isCompletedNormally()) {
//			System.out.printf("therr have %d targets\n",task.join());	
//		} else {
//			System.out.println(task.getException());
//		}
	}
}

class Task extends RecursiveTask<Integer> {
	
	private int[] arr;
	private int target;
	private int first;
	private int last;
	private static final int THRESHOLD = 5;
	
	public Task(int[] arr,int target) {
		
		this.arr = arr;
		this.target = target;
		first = 0;
		last = arr.length-1;
	}
	
	private Task(int[] arr,int target,int first,int last) {
		
		this.arr = arr;
		this.target = target;
		this.first = first;
		this.last = last;
	}
	
	@Override
	protected Integer compute() {
		
		if((last-first+1) <= THRESHOLD) {
			if(first==0) {
				throw new RuntimeException("exception test");
			}
			return count();
		} else {
			int mid = (first+last)/2;
			RecursiveTask<Integer> t1 = new Task(arr,target,first,mid);
			RecursiveTask<Integer> t2 = new Task(arr,target,mid+1,last);
			invokeAll(t1,t2);
			try {
				return t1.get()+t2.get();
			} catch (InterruptedException e) {
				throw new RuntimeException("the "+ThreadUtil.currentThreadName()+" has been interrupted",e);
			} catch (ExecutionException e) {
				throw new RuntimeException("执行任务时出现异常",e);
			}
		}
	}
	
	private int count() {
		
		int count = 0;
		for(int i=first;i<=last;i++) {
			if(arr[i] == target) {
				count++;
			}
		}
		return count;
	}
}