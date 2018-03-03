package chapter6.p05;

import java.util.Date;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author 付大石 
 * 实现一个延迟消息队列，指定延迟时间，在时间到达后发送消息
 */
public class TestX {
	
	public static void main(String[] args) {
		
		DelayQueue<Message> queue = new DelayQueue<Message>();
		ExecutorService executor = Executors.newCachedThreadPool();
		executor.execute(new Producer(new Message("18970072063","15083825093","qweasdzxc",3000),queue));
		executor.execute(new Producer(new Message("18970072063","15083825093","qweasdzxc",5000),queue));
		executor.execute(new Producer(new Message("18970072063","15083825093","qweasdzxc",9000),queue));
		executor.shutdown();
		try {
			executor.awaitTermination(1,TimeUnit.HOURS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for(int i=0;i<3;i++) {
			try {
				System.out.println(queue.take());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

/**
 * @author 付大石 
 * 消息bean
 */
class Message implements Delayed {

	private String text; // 发送内容
	private String from; // 发送人
	private String to; // 接收人
	private long sendTime; // 发送时间

	public Message(String from, String to, String text, long delay) {

		this.from = from;
		this.to = to;
		this.text = text;
		sendTime = System.currentTimeMillis() + delay;
	}

	@Override
	public int compareTo(Delayed delayed) {

		long delay = delayed.getDelay(TimeUnit.MILLISECONDS);
		long gap = getDelay(TimeUnit.MILLISECONDS) - delay;
		if (gap < 0) {
			return -1;
		} else if (gap > 0) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * 返回剩余的激活时间
	 */
	@Override
	public long getDelay(TimeUnit timeUnit) {

		Date now = new Date();
		long delay = sendTime - now.getTime();
		return timeUnit.convert(delay, TimeUnit.MILLISECONDS);
	}

	@Override
	public String toString() {
		return "Message [text=" + text + ", from=" + from + ", to=" + to + ", sendTime=" + sendTime + "]";
	}

}

class Producer implements Runnable {

	private DelayQueue<Message> queue;
	private Message message;

	public Producer(Message message, DelayQueue<Message> queue) {

		this.message = message;
		this.queue = queue;
	}

	@Override
	public void run() {
		queue.add(message);
	}

}
