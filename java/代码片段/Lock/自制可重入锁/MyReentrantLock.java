package test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyReentrantLock implements Lock {
	
	private Lock lock;
	private final Condition holdThread;
	private int holdCount;
	private Thread exclusive;
	
	public MyReentrantLock() {
		
		lock = new ReentrantLock();
		holdThread = lock.newCondition();
	}
	
	@Override
	public void lock() {
		
		lock.lock();
		try {
			Thread currentThread = Thread.currentThread();
			if(exclusive == currentThread) { //重入(reentrant)
				holdCount++; //锁持有的线程数+1
				return ;
			}
			while(holdCount!=0) {
				try {
					holdThread.await();
				} catch (InterruptedException e) {
					throw new RuntimeException("the "+Thread.currentThread().getName()+" has been interrupted");
				}
			}
			exclusive = currentThread;
			holdCount++; //锁持有的线程数+1
		} finally {
			lock.unlock();
		}
	}
	@Override
	public void unlock() {
		
		lock.lock();
		holdCount--;
		if(holdCount==0) {
			holdThread.signalAll();
		}
		lock.unlock();
	}
	
	@Override
	public void lockInterruptibly() throws InterruptedException {
		
		throw new UnsupportedOperationException();
	}
	@Override
	public boolean tryLock() {
		
		throw new UnsupportedOperationException();
	}
	@Override
	public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
		
		throw new UnsupportedOperationException();
	}
	@Override
	public Condition newCondition() {
		
		throw new UnsupportedOperationException();
	}
}