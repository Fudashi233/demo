package chapter2.p06;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author 付大石
 * 编写一个PriceInfo类表示价格信息，一个ReaderTask和一个WriterTask分别读取和写入价格信息
 */
public class Test {
	
	public static void main(String[] args) {
		
		PriceInfo priceInfo = new PriceInfo(7,8);
		Thread r1 = new Thread(new ReaderTask(priceInfo));
		Thread r2 = new Thread(new ReaderTask(priceInfo));
		Thread r3 = new Thread(new ReaderTask(priceInfo));
		Thread r4 = new Thread(new ReaderTask(priceInfo));
		Thread r5 = new Thread(new ReaderTask(priceInfo));
		Thread r6 = new Thread(new ReaderTask(priceInfo));
//		Thread w1 = new Thread(new WriterTask(priceInfo));
		r1.start();
		r2.start();
		r3.start();
//		w1.start();
		r4.start();
		r5.start();
		r6.start();
	}
}

class PriceInfo {
	
	private final ReadWriteLock lock;
	private int price1;
	private int price2;
	public PriceInfo(int price1,int price2) {
		
		lock = new ReentrantReadWriteLock();
		this.price1 = price1;
		this.price2 = price2;
	}
	
	public int getPrice1() {
		
		lock.readLock().lock();
		int value = price1;
		lock.readLock().unlock();
		return value;
	}
	
	public int getPrice2() {
		
		lock.readLock().lock();
		lock.writeLock().lock();
		int value = price2;
		lock.writeLock().unlock();
		lock.readLock().unlock();
		return value;
	}
	
	public void setPrices(int price1,int price2) {
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		lock.writeLock().lock();
		this.price1 = price1;
		this.price2 = price2;
		System.out.printf("setPrices(%d,%d)\n",price1,price2);
		lock.writeLock().unlock();
	}
}

class ReaderTask implements Runnable {
	
	private PriceInfo priceInfo;
	public ReaderTask(PriceInfo priceInfo) {
		
		this.priceInfo = priceInfo;
	}
	@Override
	public void run() {
		
		for(int i=0;i<10;i++) {
			System.out.printf("price1=%d\n",priceInfo.getPrice1());
			System.out.printf("price2=%d\n",priceInfo.getPrice2());
		}
	}
}
class WriterTask implements Runnable {
	
	private PriceInfo priceInfo;
	public WriterTask(PriceInfo priceInfo) {
		
		this.priceInfo = priceInfo;
	}
	@Override
	public void run() {
		
		for(int i=0;i<3;i++) {
			int price1 = (int)(Math.random()*10);
			int price2 = (int)(Math.random()*10);
			priceInfo.setPrices(price1,price2);
		}
	}
}