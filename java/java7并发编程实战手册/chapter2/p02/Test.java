package chapter2.p02;


/**
 * @author 付大石
 * 编写一个拥有存钱和取钱方法的银行账号类Acount
 * 编写一个存钱任务Deposit
 * 编写一个取钱任务Draw
 * 存钱任务和取钱任务同时访问银行账号类，要求存钱和取钱互斥访问Acount的balance属性   
 */
public class Test {
	
	public static void main(String[] args) {
		
		Acount acount = new Acount(1000);
		Runnable draw = new Draw(acount,5);
		Runnable deposit = new Deposit(acount,5);
		Thread drawThread = new Thread(draw);
		Thread depositThread = new Thread(deposit);
		drawThread.start();
		depositThread.start();
		try {
			System.out.println("23---"+acount);
			drawThread.join();
			System.out.println("25---"+acount);
			depositThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("30---"+acount);
	}
}

class Acount {
	
	private int balance;
	
	public Acount(int balance) {
		
		this.balance = balance;
	}
	
	public synchronized void deposit(int amount) {
		balance += amount;
	}
	
	public synchronized void draw(int amount) {
		balance -= amount;
	}

//	public void deposit(int amount) {
//		int balanceTemp = balance+amount;
//		Thread.yield();
//		balance = balanceTemp;
//	}
//	
//	public void draw(int amount) {
//		int balanceTemp = balance-amount;
//		Thread.yield();
//		balance = balanceTemp;
//	}
	
	@Override
	public String toString() {
		
		return "balance:"+balance;
	}
}

class Deposit implements Runnable {
	
	private Acount acount;
	private int amount;
	
	public Deposit(Acount acount,int amount) {
		
		this.acount = acount;
		this.amount = amount;
	}
	
	@Override
	public void run() {
		
		for(int i=0;i<100;i++) {
			acount.deposit(amount);
		}
	}
}

class Draw implements Runnable {
	
	private Acount acount;
	private int amount;
	
	public Draw(Acount acount,int amount) {
		
		this.acount = acount;
		this.amount = amount;
	}
	
	@Override
	public void run() {
		
		for(int i=0;i<100;i++) {
			acount.draw(amount);
		}
	}
}