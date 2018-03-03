package chapter3.p08;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Exchanger;

import test.ThreadUtil;

public class Test {
	
	public static void main(String[] args) {
		
		Exchanger<List<String>> exchanger = new Exchanger<>();
		
		List<String> list1 = new ArrayList<String>();
		Producer producer = new Producer(exchanger,list1);
		
		List<String> list2 = new ArrayList<String>();
		Costumer costumer = new Costumer(exchanger,list2);
		
		Thread t1 = new Thread(costumer);
		Thread t2 = new Thread(producer);
		t1.start();
		t2.start();
	}
}

class Costumer implements Runnable {
	
	 private Exchanger<List<String>> exchanger;
	 private List<String> buffer;
	 private final int LEN = 5;
	 
	 public Costumer(Exchanger<List<String>> exchanger,List<String> buffer) {
		 
		 this.exchanger = exchanger;
		 this.buffer = buffer;
	 }
	 
	 @Override
	 public void run() {
		 
		 for(int i=0;i<10;i++) {
			 
			 buffer.clear();
			try {
				ThreadUtil.randomSleep(5000);
				buffer = exchanger.exchange(buffer);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("\t\t\t\t\t\tcostumer:after exchange"+buffer);
		 }
	 }
}

class Producer implements Runnable {
	
	private Exchanger<List<String>> exchanger;
	private List<String> buffer;
	private final int LEN = 5;
	
	public Producer(Exchanger<List<String>> exchanger,List<String> buffer) {
		
		this.exchanger = exchanger;
		this.buffer = buffer;
	}
	
	@Override
	public void run() {
		
		for(int i=0;i<10;i++) {
			for(int j=0;j<LEN;j++) {
				buffer.add(String.format("%d:add stuff%d",i,j));
			}
			System.out.println("-"+buffer);
			try {
				ThreadUtil.randomSleep(5000);
				buffer = exchanger.exchange(buffer);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("producer:after exchange"+buffer);
		}
	}
}