package chapter1.p07;

import java.util.concurrent.TimeUnit;

public class NetworkConnectionsLoader implements Runnable{
	
	@Override
	public void run() {
		System.out.println("NetworkConnectionsLoader begin");
		try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			throw new RuntimeException("the thread has been interrupted");
		}
		System.out.println("NetworkConnectionsLoader end");
	}
}
