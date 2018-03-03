package chapter3.p05;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

public class Test {
	
	public static void main(String[] args) {
		
		final int SIZE = 12;
		final int LENGTH = 100;
		final int NUM = 5;
		final int PARTITION_COUNT = 2;
		Matrix matrix = new Matrix(SIZE,LENGTH,NUM);
		Result result = new Result(SIZE);
		int partition = SIZE/PARTITION_COUNT;
		int surplus = SIZE % PARTITION_COUNT;
		if(surplus!=0) { //有剩余
			partition++;
		}
		CyclicBarrier cyclicBarrier = new CyclicBarrier(partition,new Sum(result));
		/* 创建线程 */
		Thread[] threadArr = new Thread[partition];
		for(int i=0;i<partition;i++) {
			if(i==partition-1 && surplus!=0) {
				threadArr[i] = new Thread(new Search(i*PARTITION_COUNT,(i+1)*PARTITION_COUNT+surplus-1,matrix,result,NUM,cyclicBarrier));
			} else {
				threadArr[i] = new Thread(new Search(i*PARTITION_COUNT,(i+1)*PARTITION_COUNT-1,matrix,result,NUM,cyclicBarrier));
			}
		}
		/* 开启线程 */
		for(int i=0;i<threadArr.length;i++) {
			threadArr[i].start();
		}
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		threadArr[0].interrupt();
	}
}

class Search implements Runnable {
	
	private int first; //扫描范围的边界
	private int last; //扫面范围的的边界
	private Matrix matrix;
	private Result result;
	private int number; //要寻找的数
	private final CyclicBarrier cyclicBarrier;
	public Search(int first,int last,Matrix matrix,Result result,int number,CyclicBarrier cyclicBarrier) {
		
		this.first = first;
		this.last = last;
		this.matrix = matrix;
		this.result = result;
		this.number = number;
		this.cyclicBarrier = cyclicBarrier;
	}
	
	@Override
	public void run() {
		
		System.out.printf("the %s is processing %d~%d\n",Thread.currentThread().getName(),first,last);
		for(int i=first;i<=last;i++) {
			int[] data = matrix.getData(i);
			int count = 0;
			for(int j=0;j<data.length;j++) {
				if(data[j] == number) {
					count++;
				}
			}
			result.setValue(i,count);
		}
		try {
			TimeUnit.SECONDS.sleep((int)(Math.random()*5));
			System.out.printf("the %s over %d~%d\n",Thread.currentThread().getName(),first,last);
			cyclicBarrier.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			e.printStackTrace();
		}
	}
}

class Sum implements Runnable {
	
	private Result result;
	
	public Sum(Result result) {
		
		this.result = result;
	}
	
	@Override
	public void run() {
		
		System.out.printf("the %s processing result...\n",Thread.currentThread().getName());
		int sum = 0;
		int[] data = result.getData();
		for(int i=0;i<data.length;i++) {
			sum+=data[i];
		}
		System.out.printf("the %s process over,the result is %d\n",Thread.currentThread().getName(),sum);
	}
}

class Matrix {
	
	private int[][] data;
	
	public Matrix(int size,int length,int number) {
		
		int count = 0;
		data = new int[size][length];
		for(int i=0;i<data.length;i++) {
			for(int j=0;j<data[i].length;j++) {
				int k = (int)(Math.random()*10);
				data[i][j] = k;
				if(k==number) {
					count++;
				}
			}
		}
		System.out.printf("there are %d\n",count);
	}
	
	public int[] getData(int i) {
		
		return data[i];
	}
}

class Result {
	
	private int[] result;
	public Result(int size) {
		
		result = new int[size];
	}
	
	public void setValue(int index,int count) {
		
		result[index] = count;
	}
	
	public int[] getData() {
		
		return result;
	}
	
	public int getData(int i) {
		
		return result[i];
	}
}