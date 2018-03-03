package test;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author 付大石
 * 银行出纳员仿真，银行出纳员（Teller）的数量会随着顾客（Customer）的
 * 数量变化
 */
public class Test {
	
	public static void main(String[] args) {
		
		BlockingQueue<Customer> customerQueue = new ArrayBlockingQueue<Customer>(50);
		ExecutorService executor = Executors.newCachedThreadPool();
		TellerManager tellerManager = new TellerManager(executor,customerQueue);
		executor.execute(new CustomerGenerator(customerQueue));
		executor.execute(tellerManager);
	}
	
	private static class Customer {
		
		private final long serviceTime;
		
		public Customer(long serviceTime) {
			this.serviceTime = serviceTime;
		}
		
		@Override
		public String toString() {
			return String.format("[Customer:serviceTime=%d]",serviceTime);
		}
	}

	private static class CustomerGenerator implements Runnable {
		
		private BlockingQueue<Customer> customerQueue;
		private static final long GAP = 300;
		private static final long SERVICE_TIME = 1000;
		
		public CustomerGenerator(BlockingQueue<Customer> customerQueue) {
			this.customerQueue = customerQueue;
		}
		
		@Override
		public void run() {
			
			try {
				while(!ThreadUtil.curIsInterrupted()) {
					ThreadUtil.randomSleep(GAP);
					Customer customer = new Customer((long)(Math.random()*SERVICE_TIME));
					customerQueue.put(customer);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static class Teller implements Runnable,Comparable<Teller> {

		private String name; //出纳员的名称
		private int count; //处理的顾客数量
		private BlockingQueue<Customer> customerQueue; //顾客队列
		private boolean isServing; //是否正在服务
		
		public Teller(String name,BlockingQueue<Customer> customerQueue) {
			
			this.name = name;
			this.customerQueue = customerQueue;
			isServing = true;
		}
		
		
		
		@Override
		public int compareTo(Teller teller) {
			
			if(this.count<teller.count) {
				return -1;
			} else if(this.count>teller.count) {
				return 1;
			} else {
				return 0;
			}
		}

		@Override
		public void run() {
			
			try {
				while(!ThreadUtil.curIsInterrupted()) {
					Customer customer = customerQueue.take();
					Thread.sleep(customer.serviceTime);
					synchronized(this) {
						count++;
						while(!isServing) {
							super.wait();
						}
					}
				}
			} catch (InterruptedException e) {
				throw new RuntimeException("the "+name+" has been interrupted",e);
			}
		}
		
		public void rest() {
			
			count = 0;
			isServing = false;
		}
		
		public synchronized void serve() {
			
			if(isServing) { //？？？删掉会唤醒无用的线程，影响性能，但不是必要的
				return ;
			}
			isServing = true;
			super.notifyAll();
		}
		
		@Override
		public String toString() {
			return String.format("[Teller:name=%s;count=%d;isServing=%b]",name,count,isServing);
		}
	}
	
	private static class TellerManager implements Runnable {
		
		private static final long ADJUSTMENT_PERIOD = 1000; //调整周期
		private ExecutorService executor;
		private BlockingQueue<Customer> customerQueue;
		private PriorityQueue<Teller> serveTellerQueue;
		private Queue<Teller> restTellerQueue;
		
		public TellerManager(ExecutorService executor,BlockingQueue<Customer> customerQueue) {
			
			this.executor = executor;
			this.customerQueue = customerQueue;
			serveTellerQueue = new PriorityQueue<>();
			restTellerQueue = new LinkedList<>();
			/* 初始化一个Teller任务放入executor中 */
			Teller teller = new Teller("init",customerQueue);
			serveTellerQueue.add(teller);
			executor.execute(teller);
		}
		
		/**
		 * 调整Teller的数量
		 * 1.当Customer的数量是服务状态的Teller的两倍以上，则增加Teller的数量
		 * 2.当Customer的数量是服务状态的Teller的两倍以下，则减少Teller的数量
		 * 3.当Customer的数量是0时，serveTellerQueue中仅留一名Teller
		 */
		public void adjust() {
			
			if(customerQueue.size()/serveTellerQueue.size()>2) { //增加Teller的数量
				if(restTellerQueue.isEmpty()) { //如果无休息的Teller
					Teller t = new Teller("adjust",customerQueue);
					serveTellerQueue.add(t);
					executor.execute(t);
				} else {
					adjustRest();
				}
				return ;
			}
			
			if(customerQueue.size()/serveTellerQueue.size()<2) { //减少Teller的数量
				if(serveTellerQueue.size()>1) {
					adjustServe();
				}
				return ;
			}
			
			if(customerQueue.size()==0) { //仅留一名Teller
				while(serveTellerQueue.size()>1) {
					adjustServe();
				}
			}
		}
		
		public void adjustServe() {
			
			Teller t = serveTellerQueue.remove();
			t.rest();
			restTellerQueue.add(t);
		}
		
		public void adjustRest() {
			
			Teller t = restTellerQueue.remove();
			t.serve();
			serveTellerQueue.add(t);
		}
		
		@Override
		public void run() {
			
			try {
				while(!ThreadUtil.curIsInterrupted()) {
					Thread.sleep(ADJUSTMENT_PERIOD);
					adjust();
					printInfo();
				}
			} catch (InterruptedException e) {
				throw new RuntimeException("the "+ThreadUtil.curThreadName()+" has been interrupted",e);
			}
		}
		
		public void printInfo() {
			
			System.out.println("customerQueue size:"+customerQueue.size());
			System.out.println("serveTellerQueue size:"+serveTellerQueue.size());
			System.out.println("restTellerQueue size:"+restTellerQueue.size());
			System.out.println();
		}
	}
}