package chapter6.p08;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import test.ThreadUtil;

public class Test {
	
	public static void main(String[] args) {
		
		Account account = new Account(1000);
		ExecutorService executor = Executors.newCachedThreadPool();
		executor.execute(new DepositTask(account));
		executor.execute(new DrawTask(account));
		executor.shutdown();
		try {
			executor.awaitTermination(1,TimeUnit.HOURS);
		} catch(InterruptedException ex) {
			throw new RuntimeException("the "+ThreadUtil.currentThreadName()+" has been interrupted",ex);
		}
		System.out.println(account.getBalance());
	}
}

class Account {
	
	private AtomicInteger balance;
	
	public Account(int balance) {
		this.balance = new AtomicInteger(balance);
	}
	
	public void deposit(int i) {
		balance.getAndAdd(i);
	}
	
	public void draw(int i) {
		balance.getAndAdd(-i);
	}
	
	public void setBalance(int balance) {
		this.balance = new AtomicInteger(balance);
	}
	
	public int getBalance() {
		return balance.get();
	}
}

class DrawTask implements Runnable {
	
	private Account account;
	
	public DrawTask(Account account) {
		this.account = account;
	}
	
	@Override
	public void run() {
		
		for(int i=0;i<1000;i++) {
			account.draw(i);
		}
	}
}
class DepositTask implements Runnable {
	
	private Account account;
	
	public DepositTask(Account account) {
		this.account = account;
	}
	
	public void run() {
		
		for(int i=0;i<1000;i++) {
			account.deposit(i);
		}
	}
}
