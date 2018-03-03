package chapter2.p05;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author 付大石
 * 编写一个Runnable实现类Job，他调用PrintQueue.print()模拟打印工作
 * 先有多个Job一个PrintQueue，要求各个job之间互斥访问
 */
public class Test {
	
	public static void main(String[] args) {
		
		Printer printer = new Printer();
		for(int i=0;i<10;i++) {
			new Thread(new PrintTask(printer)).start();
		}
	}
}


class PrintTask implements Runnable {
	
	private Printer printer;
	public PrintTask(Printer printer) {
		
		this.printer = printer;
	}
	@Override
	public void run() {
		
		printer.print();
	}
}

class Printer {
	
	private final Lock lock;
	public Printer() {
		
		lock = new ReentrantLock();
	}
	public void print() {
		
		if(lock.tryLock()) {
			
			System.out.printf("The %s get lock\n",Thread.currentThread().getName());
			System.out.printf("The %s going to print a document\n",Thread.currentThread().getName());
			int randomSleep = (int)(Math.random()*10);
			System.out.printf("The %s is printing,spend %d seconds\n",Thread.currentThread().getName(),randomSleep);
			try {
				TimeUnit.SECONDS.sleep(randomSleep);
				System.out.printf("The %s has been printed\n",Thread.currentThread().getName());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				System.out.printf("The %s get unlock\n",Thread.currentThread().getName());
				lock.unlock();
			}
		} else {
			System.out.printf("The %s cannot get unlock\n",Thread.currentThread().getName());
		}
		
	}
}