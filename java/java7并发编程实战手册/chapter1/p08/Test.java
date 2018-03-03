package chapter1.p08;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author 付大石
 * 由于Cleaner的存在,deque中的size不会超过30
 */
public class Test {
	
	public static void main(String[] args) {
		
		Deque<Event> deque = new ArrayDeque<Event>();
		Runnable generator = new Generator(deque);
		/* 开启三个Generator线程并start */
		for(int i=0;i<3;i++) {
			Thread t = new Thread(generator);
			t.start();
		}
		
		/* 开启一个cleaner进程 */
		Thread cleaner = new Cleaner(deque);
		cleaner.start();
	}
}
