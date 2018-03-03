package chapter2.p03;

/**
 * @author 付大石
 * 一个影院有两个厅，也就有两种票，另外他还有两个售票处
 * 编写一个影院类Cinema,两个售票任务SellTask,要求两个售票任务互斥访问Cinema的ticket1和ticker2属性
 */
public class Test {
	
	public static void main(String[] args) {
		
		Cinema cinema = new Cinema(100,100);
		Runnable sellTask1 = new SellTask1(cinema);
		Runnable sellTask2 = new SellTask2(cinema);
		Thread t1 = new Thread(sellTask1);
		Thread t2 = new Thread(sellTask2);
		t1.start();
		t2.start();
		try {
			t1.join();
			t2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(cinema);
	}
}

class Cinema {
	
	private int ticket1;
	private int ticket2;
	private Object sell1Controler;
	private Object sell2Controler;
	public Cinema(int ticket1,int ticket2) {
		
		this.ticket1 = ticket1;
		this.ticket2 = ticket2;
		sell1Controler = new Object();
		sell2Controler = new Object();
	}
	
	public void sell1(int amount) {
		
		synchronized(sell1Controler) { //创建sell1的临界区
			if(amount>ticket1) {
				throw new IllegalArgumentException("amount超额，票数不足");
			} else {
				ticket1 -= amount;
			}
		}
	}
	
	public void sell2(int amount) {
		
		synchronized(sell2Controler) {
			if(amount>ticket2) {
				throw new IllegalArgumentException("amount超额，票数不足");
			} else {
				ticket2 -= amount;
			}
		}
	}
	
	public void return1(int amount) {
		
		synchronized(sell1Controler) { //创建sell1的临界区
			ticket1 += amount;
		}
	}
	
	public void return2(int amount) {
		
		synchronized(sell2Controler) {
			ticket2 += amount;
		}
	}
	
	@Override
	public String toString() {
		
		return "ticket1="+ticket1+";ticket2="+ticket2;
	}
}

class SellTask1 implements Runnable {
	
	private Cinema cinema;
	public SellTask1(Cinema cinema) {
		this.cinema = cinema;
	}
	
	/**
	 * -3
	 * -6
	 */
	@Override
	public void run() {
		
		cinema.sell1(1);
		cinema.sell2(2);
		cinema.sell2(1);
		cinema.sell1(2);
		cinema.sell2(2);
		cinema.sell2(1);
		cinema.return1(2);
		cinema.sell1(1);
		cinema.sell1(1);
	}
}

class SellTask2 implements Runnable {
	
	private Cinema cinema;
	public SellTask2(Cinema cinema) {
		this.cinema = cinema;
	}
	
	/**
	 * -7
	 * -2
	 */
	@Override
	public void run() {
		
		cinema.sell2(1);
		cinema.sell2(2);
		cinema.sell1(1);
		cinema.sell1(2);
		cinema.sell1(2);
		cinema.sell2(1);
		cinema.return2(2);
		cinema.sell1(1);
		cinema.sell1(1);
	}
}
