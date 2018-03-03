package chapter3.p04;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author 付大石
 * 使用CountDownLatch模拟一个会议，要求人员到齐才开始
 */
public class Test {
	
	public static void main(String[] args) {
		
		Conference conference = new Conference(5);
		Participant participant = new Participant(conference);
		Thread t1 = new Thread(conference);
		Thread t2 = new Thread(participant);
		Thread t3 = new Thread(participant);
		Thread t4 = new Thread(participant);
		Thread t5 = new Thread(participant);
		Thread t6 = new Thread(participant);
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		t5.start();
		t6.start();
	}
}

class Conference implements Runnable {
	
	private final CountDownLatch countDownLatch;
	public Conference(int count) {
		countDownLatch = new CountDownLatch(count);
	}
	
	public synchronized void arrive(String name) {
		
		countDownLatch.countDown();
		System.out.printf("the %s has arrived,wait for %d participants\n",name,countDownLatch.getCount());
	}
	@Override
	public void run() {
		
		try {
			countDownLatch.await();
			System.out.println("Let's begin");
		} catch (InterruptedException e) {
			throw new RuntimeException("the "+Thread.currentThread().getName()+" has been interrupted",e);
		}
	}
}

class Participant implements Runnable {
	
	private Conference conference;
	public Participant(Conference conference) {
		
		this.conference = conference;
	}
	@Override
	public void run() {
		
		int num = (int)(Math.random()*10);
		try {
			TimeUnit.SECONDS.sleep(num);
		} catch (InterruptedException e) {
			throw new RuntimeException("the "+Thread.currentThread().getName()+" has been interrupted",e);
		}
		conference.arrive(Thread.currentThread().getName());
	}
	
}

