package chapter6.p05;

import java.util.Date;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import test.ThreadUtil;

public class Test {
	
	public static void main(String[] args) {
		
		DelayQueue<Event> delayQueue = new DelayQueue<Event>();
		ExecutorService executor = Executors.newCachedThreadPool();
		executor.execute(new Task(delayQueue));
		executor.shutdown();
		try {
			executor.awaitTermination(1,TimeUnit.HOURS);
		} catch (InterruptedException e) {
			throw new RuntimeException("the "+ThreadUtil.currentThreadName()+" has been interrupted");
		}
		for(int i=0;i<5;i++) {
			int count = 0;
			Event event = null;
			while((event=delayQueue.poll())!=null) {
				count++;
			}
			System.out.println(count);
			try {
				TimeUnit.MILLISECONDS.sleep(1000);
			} catch (InterruptedException e) {
				throw new RuntimeException("the "+ThreadUtil.currentThreadName()+" has been interrupted");
			}
		}
	}
}

class Task implements Runnable {
	
	DelayQueue<Event> delayQueue;
	
	public Task(DelayQueue<Event> delayQueue) {
		this.delayQueue = delayQueue;
	}
	
	@Override
	public void run() {
		
		for(int i=0;i<100;i++) {
			Date date = new Date();
			date.setTime(date.getTime()+(long)(Math.random()*5000));
			delayQueue.put(new Event(date));
		}
	}
}

class Event implements Delayed {
	
	private Date delay;
	
	public Event(Date delay) {
		this.delay = delay;
	}
	
	@Override
	public int compareTo(Delayed delayed) {
		
		long nanoseconds = delayed.getDelay(TimeUnit.NANOSECONDS);
		long gap = getDelay(TimeUnit.NANOSECONDS)-nanoseconds;
		if(gap<0) {
			return -1;
		} else if(gap>0) {
			return 1;
		} else {
			return 0;
		}
	}

	
	/**
	 * 获取delay与当前时间的差值的差值
	 */
	@Override
	public long getDelay(TimeUnit unit) {
		
		Date now = new Date();
		long gap = delay.getTime()-now.getTime();
		return unit.convert(gap,TimeUnit.MICROSECONDS);
	}
	
}