package chapter1.p4;

import java.util.concurrent.TimeUnit;

public class Test {
	
	public static void main(String[] args) {
		
		Thread t = new PrimeGenerator();
		t.start();
		try {
			Thread.sleep(1000); //睡眠1000ms
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		t.interrupt();
	}
}
