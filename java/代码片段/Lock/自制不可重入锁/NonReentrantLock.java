package test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author 付大石
 * 不可重入锁
 */
public class NonReentrantLock implements Lock {
	
	private Lock lock;
	private Condition hold;
	private boolean hasThread; //如果该锁被某线程持有，则为true
	
	public NonReentrantLock() {
		
		lock = new ReentrantLock();
		hold = lock.newCondition();
	}
	
	@Override
	public void lock() {
		
		lock.lock();
		try {
			while(hasThread) {
				hold.await();
			}
			hasThread = true;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}


	
	@Override
	public void unlock() {

		lock.lock();
		hasThread = false;
		hold.signalAll();
		lock.unlock();
		
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
	
	@Override
	public void lockInterruptibly() throws InterruptedException {
		throw new UnsupportedOperationException();
	}
}
