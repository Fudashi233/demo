package chapter2.p07;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Test {
	
	public static void main(String[] args) {
		
		Printer printer = new Printer();
		Thread[] threadArr = new Thread[10];
		for(int i=0;i<threadArr.length;i++) {
			threadArr[i] = new Thread(new PrintTask(printer),""+i);
		}
		try {
			for(int i=0;i<threadArr.length;i++) {
				threadArr[i].start();
				Thread.sleep(200);
			}
		} catch (InterruptedException e) {
			throw new RuntimeException("the "+Thread.currentThread().getName()+" has been interrupted",e);
		}
		
	}
}

class Printer {
	
	private final Lock lock;
	public Printer() {
		
		lock = new ReentrantLock(true);
	}
	
	public void print() {
		
		lock.lock();
		try {
			Thread.sleep((int)(Math.random()*3000));
			System.out.printf("the %s is printing\n",Thread.currentThread().getName());
		} catch (InterruptedException e) {
			throw new RuntimeException("the "+Thread.currentThread().getName()+" has been interrupted",e);
		}
		lock.unlock();
		lock.lock();
		try {
			Thread.sleep((int)(Math.random()*3000));
			System.out.printf("the %s is printing\n",Thread.currentThread().getName());
		} catch (InterruptedException e) {
			throw new RuntimeException("the "+Thread.currentThread().getName()+" has been interrupted",e);
		}
		lock.unlock();
	}
}

class PrintTask implements Runnable {
	
	private Printer printer;
	public PrintTask(Printer        printer) {
		
		this.printer = printer;
	}
	@Override
	public void run() {
		
		System.out.printf("the %s start to print\n",Thread.currentThread().getName());
		printer.print();
		System.out.printf("the %s over to print\n",Thread.currentThread().getName());
	}
}