            package chapter4.p05;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import test.ThreadUtil;

public class Test {
	
	public static void main(String[] args) {
		
		Collection<Validator> collection = new ArrayList<Validator>();
		Collections.addAll(collection,new Validator("Peter","123"),new Validator("Clive","123"),new Validator("Olive","123"));
		ExecutorService executor = Executors.newCachedThreadPool();
		try {
			String result = executor.invokeAny(collection);
			System.out.println(result);
			executor.shutdown();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		System.out.printf("Main: End of the Execution\n");
	}
}

class Validator implements Callable<String> {
	
	private String username;
	private String password;
	public Validator(String username,String password) {
		
		this.username = username;
		this.password = password;
	}
	@Override
	public String call() throws Exception {
		
		try {
			ThreadUtil.randomSleep(10000);
		} catch(InterruptedException e) {
			throw new RuntimeException("the "+Thread.currentThread().getName()+" has been interrupted",e);
		}
		return username;
	}
}