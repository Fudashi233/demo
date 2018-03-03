package chapter4.p06;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import test.ThreadUtil;

public class Test {
	
	public static void main(String[] args) {
		
		Collection<Task> collection = new ArrayList<>();
		Collections.addAll(collection,new Task(new Result("A","1")),new Task(new Result("B","2")),new Task(new Result("C","3")));
		ExecutorService executor = Executors.newCachedThreadPool();
		try {
			List<Future<Result>> resultList = executor.invokeAll(collection);
			System.out.println("invoke all callable");
			for(int i=0;i<resultList.size();i++) {
				Result r = resultList.get(i).get();
				System.out.println(r.getKey()+"	"+r.getValue());
			}
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		
	}
}


class Task implements Callable<Result> {
	
	private Result result;
	public Task(Result result) {
		this.result = result;
	}
	@Override
	public Result call() throws Exception{
		
		try {
			ThreadUtil.randomSleep(10000);
		} catch(InterruptedException ex) {
			throw new RuntimeException("the "+Thread.currentThread().getName()+" has been interrupted",ex);
		}
		return result;
	}
}

class Result {
	
	private String key;
	private String value;
	
	public Result(String key,String value) {
		
		this.key = key;
		this.value = value;
	}
	
	public String getKey() {
		return this.key;
	}
	
	public String getValue() {
		return this.value;
	}
}