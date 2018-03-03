package chapter3.p02;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author 付大石
 * 使用二进制信号量semaphore构造一个临界区
 */
public class Test {
	
	public static void main(String[] args) {
		
		Printer printer = new Printer();
		PrintTask task = new PrintTask(printer);
		Thread[] threadArr = new Thread[10];
		for(int i=0;i<threadArr.length;i++) {
			threadArr[i] = new Thread(task);
		}
		for(int i=0;i<threadArr.length;i++) {
			threadArr[i].start();
		}
	}
}

class Printer {
	
	private final Semaphore semaphore;
	public Printer() {
		
		semaphore = new Semaphore(1);
	}
	
	public void print() {
		
		try {
			semaphore.acquire();
			int duration = (int)(Math.random()*10);
			System.out.printf("the %s wait for %d seconds\n",Thread.currentThread().getName(),duration);
			TimeUnit.SECONDS.sleep(duration);
		} catch (InterruptedException e) {
			throw new RuntimeException("the "+Thread.currentThread().getName()+" has been interrupted");
		} finally {
			semaphore.release();
		}
	}
}

class  PrintTask implements Runnable {
	
	private Printer printer;
	public PrintTask(Printer printer) {
		
		this.printer = printer;
	}
	@Override
	public void run() {
		
		System.out.printf("the %s is going to print\n",Thread.currentThread().getName());
		printer.print();
		System.out.printf("the %s is over\n",Thread.currentThread().getName());
	}
}