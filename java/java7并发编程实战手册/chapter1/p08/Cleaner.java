package chapter1.p08;

import java.util.Date;
import java.util.Deque;
import java.util.concurrent.TimeUnit;

public class Cleaner extends Thread{
	
	private Deque<Event> deque;
	private static final int DIFFERENCE = 5000;
	public Cleaner(Deque<Event> deque) {
		setDaemon(true); //守护进程
		this.deque = deque;
	}
	
	@Override
	public void run() {

		while(true) {
			clean();
		}
	}
	
	private void clean() {
		
		if(deque.isEmpty()) {
			return ;
		}
		System.out.println("---"+deque.size());
		long now = new Date().getTime();
		long difference = now-deque.getFirst().getDate().getTime();
		boolean delete = false;
		while(difference>DIFFERENCE) {
			System.out.println("---");
			deque.removeFirst();
			delete = true;
			if(!deque.isEmpty()) {
				difference = now-deque.getFirst().getDate().getTime();
			} else {
				break;
			}
		}
		if(delete) {
			System.out.println("deque size:"+deque.size());
		}
	}
}
