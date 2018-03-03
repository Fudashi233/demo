package test;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * @author 付大石
 * 加锁顺序不一致
 */
public class Test {
	
	private static Lock lock1 = new ReentrantLock();
	private static Lock lock2 = new ReentrantLock();
	public static void main(String[] args) throws InterruptedException {
		
		Thread t1 = new Thread(new Task(1),"t1");
		Thread t2 = new Thread(new Task(2),"t2");
		t1.start();
		t2.start();
		Thread.sleep(5000);
		System.out.println("中断t1线程");
		t1.interrupt(); //注释掉会产生死锁
	}

	private static class Task implements Runnable {
		
		private int order;

		public Task(int order) {
			this.order = order;
		}
		
		@Override
		public void run() {
			
			try {
				if(order==1) {
					lock1.lockInterruptibly();
					Thread.sleep(500);
					lock2.lockInterruptibly();
				} else {
					lock2.lockInterruptibly();
					Thread.sleep(500);
					lock1.lockInterruptibly();
				}
			} catch (InterruptedException e) {
				throw new RuntimeException("the "+ThreadUtil.currentThreadName()+" has been interrupted",e);
			} finally {
				if(((ReentrantLock)lock1).isHeldByCurrentThread()) {
					lock1.unlock();
				}
				if(((ReentrantLock)lock2).isHeldByCurrentThread()) {
					lock2.unlock();
				}
				System.out.println(ThreadUtil.currentThreadName()+" exit");
			}
		}
	}
}






package test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * @author 付大石
 */
public class Test {
	
	private static Lock lock1 = new ReentrantLock();
	private static Lock lock2 = new ReentrantLock();
	public static void main(String[] args) throws InterruptedException {
		
		Runnable runnable1 = new Task(1);
		Runnable runnable2 = new Task(2);
		Thread t1 = new Thread(runnable1,"t1");
		Thread t2 = new Thread(runnable2,"t2");
		t1.start();
		t2.start();
	}

	private static class Task implements Runnable {

		private int order;
		public Task(int order) {
			this.order = order;
		}
		@Override
		public void run() {
			
			if(order==1) {
				while(true) {
					if(lock1.tryLock()) {
						try {
							TimeUnit.MILLISECONDS.sleep(500);
							if(lock2.tryLock()) {
								System.out.println(ThreadUtil.currentThreadName()+" get locks success");
								return ;
							}
						} catch(InterruptedException ex) {
							throw new RuntimeException("the "+ThreadUtil.currentThreadName()+" has been interrupted",ex);
						} finally {
							lock1.unlock();
							if(((ReentrantLock)lock2).isHeldByCurrentThread()) {
								lock2.unlock();
							}
							System.out.println(ThreadUtil.currentThreadName()+" exit");
						}
					}
				}
			} else {
				while(true) {
					if(lock2.tryLock()) {
						try {
							TimeUnit.MILLISECONDS.sleep(500);
							if(lock1.tryLock()) {
								System.out.println(ThreadUtil.currentThreadName()+" get locks success");
								return ;
							}
						} catch(InterruptedException ex) {
							throw new RuntimeException("the "+ThreadUtil.currentThreadName()+" has been interrupted",ex);
						} finally {
							if(((ReentrantLock)lock1).isHeldByCurrentThread()) {
								lock1.unlock();
							}
							lock2.unlock();
							System.out.println(ThreadUtil.currentThreadName()+" exit");
						}
					}
				}
			}
		}
	}
}