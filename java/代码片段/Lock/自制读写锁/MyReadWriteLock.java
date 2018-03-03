package test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;

public class MyReadWriteLock implements ReadWriteLock{

	private final Lock lock;
	private final Condition exitWriteCondition;
	private final Condition exitReadCondition;
	private final Lock readLock;
	private final Lock writeLock;
	private volatile boolean write; //记录是否是写线程
	private volatile int readCount; //记录读线程的个数
	public MyReadWriteLock() {
		
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
				while(write) { //如果写锁存在
					try {
						exitWriteCondition.await();
					} catch (InterruptedException e) {
						throw new RuntimeException("the "+Thread.currentThread().getName()+" has been interrupted",e);
					}
				}
				readCount++;
			} finally {
				lock.unlock();
			}
		}
		
		@Override
		public void unlock() {
			
			lock.lock();
			readCount--;
			if(readCount==0) {
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
				while(readCount!=0) { //有其他写线程存在 或者 有其他读线程存在
					try {
						exitReadCondition.await();
					} catch (InterruptedException e) {
						throw new RuntimeException("the "+Thread.currentThread().getName()+" has been interrupted");
					}
					
				}
				while(write) {
					try {
						exitWriteCondition.await();
					} catch (InterruptedException e) {
						throw new RuntimeException("the "+Thread.currentThread().getName()+" has been interrupted");
					}
				}
				write = true;
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
