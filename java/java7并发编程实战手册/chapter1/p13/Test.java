package chapter1.p13;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class Test {
	
	public static void main(String[] args) {
		
		MyThreadFactory factory = new MyThreadFactory();
		Runnable task = new Task();
		for(int i=0;i<10;i++) {
			factory.newThread(task).start();
		}
		factory.printInfo();
	}
}


class MyThreadFactory implements ThreadFactory {
	
	private List<Thread> list;
	public MyThreadFactory() {
		list = new ArrayList<Thread>();
	}
	@Override
	public Thread newThread(Runnable r) {

		Thread t = new Thread(r);
		list.add(t);
		return t;
	}
	
	public void printInfo() {
		
		for(int i=0;i<list.size();i++) {
			System.out.println(list.get(i));
		}
	}
}

class Task implements Runnable {

	@Override
	public void run() {
		
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

