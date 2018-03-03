package chapter1.p07;

import java.util.Date;

/**
 * @author 付大石
 * 当一个线程的join()方法被调用，调用这个方法的线程会被阻塞直至其运行结束
 * 当一个线程的join(long)方法被调用，调用这个方法的线程会被阻塞相应的时间
 */
public class Test {
	
	public static void main(String[] args) {
		
		Thread t1 = new Thread(new DataSourcesLoader());
		Thread t2 = new Thread(new NetworkConnectionsLoader());
		t1.start();
		t2.start();
//		try {
//			t1.join();
//			t2.join();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		System.out.printf("configuration has been loaded: %s\n",new Date()); //如果没有join，这行代码就立马执行了
	}
}
