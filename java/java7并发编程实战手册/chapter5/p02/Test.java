package chapter5.p02;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

import test.ThreadUtil;

public class Test {
	
	public static void main(String[] args) {
		
		/* 生产1000个product并加入productList中 */
		List<Product> productList = new LinkedList<Product>();
		for(int i=0;i<1000;i++) {
			Product product = new Product();
			product.setName("product-"+i);
			product.setPrice(10);
			productList.add(product);
		}
		Task task = new Task(productList,0.5d);
		ForkJoinPool forkJoinPool = new ForkJoinPool();
		forkJoinPool.execute(task);
		forkJoinPool.shutdown();
		while(!task.isDone()) {
			
			System.out.printf("pool size:%d\n",forkJoinPool.getPoolSize());
			System.out.printf("thread count:%d\n",forkJoinPool.getActiveThreadCount());
			System.out.printf("thread steal:%d\n",forkJoinPool.getStealCount());
			System.out.printf("parallelism:%d\n",forkJoinPool.getParallelism());
			System.out.println("-------------------------------");
			try {
				ThreadUtil.sleep(500);
			} catch (InterruptedException e) {
				throw new RuntimeException("the "+ThreadUtil.currentThreadName()+" has been interrupted",e);
			}
		}
		if(task.isCompletedNormally()) {
			for(int i=0;i<5;i++) {
				System.out.println(productList.get(0));
			}
		} else {
			throw new RuntimeException("the process has completed abnormaly",task.getException());
		}
		
	}
}

class Task extends RecursiveAction {
	
	private List<Product> productList;
	private double rate;
	private int first;
	private int last;
	private static final int THRESHOLD = 10;
	public Task(List<Product> productList, double rate) {
		this.productList = productList;
		this.rate = rate;
		first = 0;
		last = productList.size()-1;
	}
	
	private Task(List<Product> productList,double rate,int first,int last) {
		
		this.productList = productList;
		this.rate = rate;
		this.first = first;
		this.last = last;
	}
	
	@Override
	protected void compute() {
		
		if((last-first+1) <= Task.THRESHOLD) {
			updatePrice();
		} else { //超出THRESHOLD指定的规模，对任务进行fork
			int mid = (last+first)/2;
			Task t1 = new Task(productList,rate,first,mid);
			Task t2 = new Task(productList,rate,mid+1,last);
			invokeAll(t1,t2);
		}
	}
	
	private void updatePrice() {
		
		for(int i=first;i<=last;i++) {
			Product product = productList.get(i);
			product.setPrice(product.getPrice()*(1+rate));
		}
	}
}

class Product {
	
	private String name;
	private double price;
	
	public Product() {
		
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "Product [name=" + name + ", price=" + price + "]";
	}
}