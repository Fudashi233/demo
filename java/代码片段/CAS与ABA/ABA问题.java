package test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;


/**
 * @author 付大石
 * 使用CAS(compare and swap)实现无锁并发。举个例子，比如要将A更新为B，更新之前需要比较下A是否还是A(即没有被别的线程改变),
 * 如果还是则更新为B,如果不是则返回false表示更新失败。CAS凭借这套机制实现无锁的安全并发，但是有个问题，请看下面。
 * 现在任然是要将A更新为B，现有两个线程T1和T2，T1是将A更新为A^，T2是将A更新为B，设A=A^。
 * 1.T2首先占用CPU，获取A
 * 2.T1抢占CPU，获取A，然后进行CAS操作，将A更新为A^
 * 3.T2再次抢占CPU，拿第一部获取的A与变量A^进行比较，发现相等，则进行更新，A^更新为B
 * 这就是ABA问题，简单讲就是由于CAS的比较操作因为只能判断*值是否变更*而无法判断*值是否被别的线程更改*了而产生的问题。
 * 对于基本类型，这在一般情况下不会出问题，但是对于引用类型，其内部的属性就算被别的线程修改了，但引用还是那个引用，
 * 这时，ABA问题就真的是个让人头痛的东西了
 * 
 * 假设有多个充值线程同时往一个账号中充值，当账号中的余额小于20元时就充值20元进去，当然只能充值一次
 * 多个线程同时工作时如果没有消费线程，那么程序运行正常，只有一个充值线程成功充值
 * 如果同时又有消费线程在工作，那么就说不定了
 * recharge1							recharge2							costume
 * 1.getMoney=19						getMoney=19							getMoney=19
 * 2.CAS成功，充值20元
 * 3.																		消费10元
 * 4.																		消费10元
 * 5.									CAS成功(仍然是19),充值20元
 */
//public class Test {
//	
//	private static Account account = new Account(new AtomicReference<Integer>(18));
//	
//	public static void main(String[] args) throws InterruptedException {
//		
//		ExecutorService executor = Executors.newFixedThreadPool(201);
//		((ThreadPoolExecutor)executor).prestartAllCoreThreads();
//		executor.execute(new Consume());
//		for(int i=0;i<200;i++) {
//			executor.execute(new Recharge());
//		}
//		executor.shutdown();
//		executor.awaitTermination(5,TimeUnit.SECONDS);
//	}
//	
//	/**
//	 * @author 付大石
//	 * 充值任务，不断寻找余额小于20元的账户，发现即向其账户充值20元以刺激消费
//	 */
//	private static class Recharge implements Runnable {
//
//		@Override
//		public void run() {
//			
//			while(true) {
//				Integer m = account.getMoney();
//				if(m<20) {
//					if(account.setMoney(m+20)) {
//						System.out.println("充值成功:"+account.getMoney());
//						break;
//					}
//				} else {
////							System.out.println("无需充值:"+money);
//					break;
//				}
//			}
//		}
//	}
//	
//	
//	/**
//	 * @author 付大石
//	 * 消费任务，每次消费10元
//	 */
//	private static class Consume implements Runnable {
//		
//		@Override
//		public void run() {
//			
//			try {
//				Thread.sleep(50);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			for(int i=0;i<100;i++) {
//				while(true) {
//					Integer m = account.getMoney();
//					if(m>=10) {
//						if(account.setMoney(m-10)) {
//							System.out.println("消费成功:"+account.getMoney());
//							 break;
//						}
//					} else {
////						System.out.println("余额不足:"+money);
//						break;
//					}
//				}
//			}
//		}
//	}
//	
//	private static class Account {
//		
//		private AtomicReference<Integer> money;
//		public Account(AtomicReference<Integer> money) {
//			this.money = money;
//		}
//		public boolean setMoney(int money) {
//			return this.money.compareAndSet(getMoney(),money);
//		}
//		public int getMoney() {
//			return money.get();
//		}
//	}
//}




public class Test {
	
	private static AtomicReference<Integer> money = new AtomicReference<Integer>(new Integer(19));
	public static void main(String[] args) throws InterruptedException {
		
		ExecutorService executor = Executors.newFixedThreadPool(3);
		((ThreadPoolExecutor)executor).prestartAllCoreThreads();
		executor.execute(new Recharge1());
		executor.execute(new Recharge2());
		executor.execute(new Costume());
		executor.shutdown();
		executor.awaitTermination(1,TimeUnit.HOURS);
	}
	
	private static class Recharge1 implements Runnable {

		@Override
		public void run() {
			
			while(true) {
				Integer m = money.get();
				if(m<20) {
					if(money.compareAndSet(m,m+20)) {
						System.out.println("充值20元，剩余"+money.get());
						break;
					}
				}
			}
		}
	}
	
	private static class Recharge2 implements Runnable {
		
		@Override
		public void run() {
			
			while(true) {
				Integer m = money.get();
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(m<20) {
					if(money.compareAndSet(m,m+20)) {
						System.out.println("充值20元，剩余"+money.get());
						break;
					}
				}
			}
		}
	}
	
	private static class Costume implements Runnable {
		
		@Override
		public void run() {
			
			while(true) {
				int m = money.get();
				if(m>=10) {
					if(money.compareAndSet(m,m-20)) {
						System.out.println("消费20元，剩余:"+money.get());
						break;
					}
				}
			}
		}
	}
}






/**
 * @author 付大石
 * 利用戳子(stamp)标记每一个引用，当进行CAS操作进行比较时，还得比较戳子，以判断当前的引用时候被其他线程修改过
 */
//public class Test {
//
//	private static Account account = new Account(new AtomicStampedReference<Integer>(18,0));
//	public static void main(String[] args) throws InterruptedException {
//		
//		ExecutorService executor = Executors.newFixedThreadPool(4);
//		((ThreadPoolExecutor)executor).prestartAllCoreThreads();
//		executor.execute(new Recharge());
//		executor.execute(new Recharge());
//		executor.execute(new Recharge());
//		executor.execute(new Consume());
//		executor.shutdown();
//		executor.awaitTermination(5,TimeUnit.SECONDS);
//	}
//
//	/**
//	 * @author 付大石
//	 * 充值任务，不断寻找余额小于20元的账户，发现即向其账户充值20元以刺激消费
//	 */
//	private static class Recharge implements Runnable {
//	
//		@Override
//		public void run() {
//			int stamp = account.getStamp();
//			while(true) {
//				while(true) {
//					int m = account.getMoney();
//					if(m<20) {
//						if(account.setMoney(m+20,stamp)) {
//							System.out.println("充值成功:"+account.getMoney());
//							break;
//						}
//					} else {
//	//						System.out.println("无需充值:"+money);
//						break;
//					}
//				}
//			}
//		}
//	}
//
//
//	/**
//	 * @author 付大石
//	 * 消费任务，每次消费10元
//	 */
//	private static class Consume implements Runnable {
//		
//		@Override
//		public void run() {
//			
//			try {
//				Thread.sleep(50);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			for(int i=0;i<100;i++) {
//				while(true) {
//					int m = account.getMoney();
//					int stamp = account.getStamp();
//					if(m>=10) {
//						if(account.setMoney(m-10,stamp)) {
//							System.out.println("消费成功:"+account.getMoney());
//							 break;
//						}
//					} else {
//	//					System.out.println("余额不足:"+money);
//						break;
//					}
//				}
//			}
//		}
//	}
//
//	private static class Account {
//		
//		private AtomicStampedReference<Integer> money;
//		public Account(AtomicStampedReference<Integer> money) {
//			this.money = money;
//		}
//		public boolean setMoney(int money,int stamp) {
//			return this.money.compareAndSet(getMoney(),money,stamp,stamp+1);
//		}
//		public int getMoney() {
//			return money.getReference();
//		}
//		public int getStamp() {
//			return money.getStamp();
//		}
//	}
//}