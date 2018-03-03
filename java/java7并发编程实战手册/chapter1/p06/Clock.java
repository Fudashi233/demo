package chapter1.p06;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Clock implements Runnable {
	
	@Override
	public void run() {
		
		for(int i=0;i<10;i++) {
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				throw new RuntimeException("The thread has been interrupted");
			}
			System.out.println(new Date());
		}
	}
}
