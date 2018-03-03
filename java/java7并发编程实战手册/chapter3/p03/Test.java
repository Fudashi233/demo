package chapter3.p03;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author 付大石
 * 使用semaphore实现资源的多副本并发访问
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
	private final int LEN = 3;
	private final Lock lock;
	private boolean[] printerArr;
	public Printer() {
		
		semaphore = new Semaphore(LEN);
		lock = new ReentrantLock();
		printerArr = new boolean[LEN];
		for(int i=0;i<printerArr.length;i++) {
			printerArr[i] = true;
		}
	}
	
	public void print() {
		
		int printerIndex = -1;
		try {
			semaphore.acquire();
			printerIndex = getPrinter();
			int duration = (int)(Math.random()*10);
			System.out.printf("the %s wait for %d seconds in %d\n",Thread.currentThread().getName(),duration,printerIndex);
			TimeUnit.SECONDS.sleep(duration);
		} catch (InterruptedException e) {
			throw new RuntimeException("the "+Thread.currentThread().getName()+" has been interrupted");
		} finally {
			printerArr[printerIndex] = true;
			semaphore.release();
		}
	}
	
	public int getPrinter() {
		
		int result = -1;
		try {
			/* 构造访问printArr的临界区 */
			lock.lock();
			for(int i=0;i<printerArr.length;i++) {
				if(printerArr[i]) {
					result = i;
					printerArr[result] = false;
					break;
				}
			}
		} finally {
			lock.unlock();
		}
		return result;
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