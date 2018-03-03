package chapter2.p04;

import java.util.ArrayDeque;
import java.util.Date;
import java.util.Deque;

/**
 * @author 付大石
 * 编写一个Storage类，拥有set()向里面放Date，拥有get()想里面拿Date
 * 编写一个Costumer
 * 编写一个Producer
 * Costumer使用Storage的get方法消费，当Storage为空时阻塞，Producer
 * 使用Storage生产，当Storage满时阻塞
 */
public class Test {
	
	public static void main(String[] args) {
		
		Storage storage = new Storage();
		Runnable costumer = new Costumer(storage);
		Runnable producer = new Producer(storage);
		Thread costumerThread = new Thread(costumer);
		Thread producerThread = new Thread(producer);
		costumerThread.start();
		producerThread.start();
		try {
			costumerThread.join();
			producerThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("F-I-N-I-S-H");
	}
}

class Storage {
	
	private static final int MAX_SIZE = 5;
	private Deque<Date> deque;//存储对象为Date
	public Storage() {
		deque = new ArrayDeque<Date>(MAX_SIZE);
	}
	
	/**
	 * @param amount
	 * 供Costumer使用
	 */
	public synchronized void get() {
		
		while(0==deque.size()) {
			try {
				System.out.printf("costumer blocked,wait for producer\n");
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		deque.pop();
		System.out.printf("costumer.get(),size=%d\n",deque.size());
		notify();
	}
	
	/**
	 * @param amount
	 * 共Producer生产
	 */
	public synchronized void set() {
		
		while(MAX_SIZE==deque.size()) {
			try {
				System.out.printf("\t\t\t\tproducer blocked,wait for costumer\n");
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		deque.push(new Date());
		System.out.printf("\t\t\t\tcostumer.set(),size=%d\n",deque.size());
		notify();
	}
}

class Costumer implements Runnable{
	
	private Storage storage;
	
	public Costumer(Storage storage) {
		
		this.storage = storage;
	}
	public void run() {
		
		for(int i=0;i<10;i++) {
			storage.get();
			storage.get();
			Thread.yield();
			storage.get();
		}
	}
}

class Producer implements Runnable{
	
	private Storage storage;
	public Producer(Storage storage) {
		
		this.storage  = storage;
	}
	
	@Override
	public void run() {
		
		for(int i=0;i<10;i++) {
			storage.set();
			storage.set();
			storage.set();
			Thread.yield();
		}
	}
}