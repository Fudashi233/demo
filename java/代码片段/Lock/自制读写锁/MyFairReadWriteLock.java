package test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;

public class MyFairReadWriteLock implements ReadWriteLock{

	private final Lock lock;
	private final Condition exitWriteCondition;
	private final Condition exitReadCondition;
	private final Lock readLock;
	private final Lock writeLock;
	private volatile boolean write; //记录是否是写线程
	private volatile int readAcquired;
	private volatile int readRelease;
	
	public MyFairReadWriteLock() {
		
		lock = new ReentrantLock();
		exitWriteCondition = lock.newCondition();
		exitReadCondition = lock.newCondition();
		readLock = new ReadLock();
		writeLock = new WriteLock();
	}
	
	
	@Override
	public Lock readLock() {
		
		return readLock;
	}

	@Override
	public Lock writeLock() {
		
		return writeLock;
	}
	
	//------------------
	// 内部类ReadLock、WriteLock
	//------------------
	class ReadLock implements Lock {
		
		public ReadLock() {
			
		}
		
		@Override
		public void lock() {
			
			lock.lock();
			try {
				while(write) {
					try {
						exitWriteCondition.await();
					} catch (InterruptedException e) {
						throw new RuntimeException("the "+Thread.currentThread().getName()+" has been interrupted",e);
					}
				}
				readAcquired++;
			}
			finally {
				lock.unlock();
			}
		}
		
		@Override
		public void unlock() {
			
			lock.lock();
			readRelease--;
			if(readRelease == readAcquired) {
				exitReadCondition.signalAll();
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
	
	class WriteLock implements Lock {
		
		public WriteLock() {
			
		}
		
		@Override
		public void lock() {
		
			lock.lock();
			try {
				while(write) {
					try {
						exitWriteCondition.await();
					} catch (InterruptedException e) {
						throw new RuntimeException("the "+Thread.currentThread().getName()+" has been interrupted",e);
					}
				}
				write = true ;//只要没有其他写锁，而不管有没有读锁置write为true,这样新加入的读线程会被阻塞
				while(readRelease != readAcquired) {
					try {
						exitReadCondition.await();
					} catch (InterruptedException e) {
						throw new RuntimeException("the "+Thread.currentThread().getName()+" has been interrupted",e);
					}
				}
			} finally {
				lock.unlock();
			}
		}
		
		@Override
		public void unlock() {
			
			lock.lock();
			write = false;
			exitWriteCondition.signalAll();
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
}
