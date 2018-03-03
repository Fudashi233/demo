package chapter2.p08;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Test {
	
	public static void main(String[] args) {
		
		Buffer buffer = new Buffer();
		FileMock fileMock = new FileMock(50);
		Runnable producer = new Producer(buffer,fileMock);
		Costumer costumer = new Costumer(buffer);
		Thread t1 = new Thread(producer);
		Thread t2 = new Thread(costumer);
		Thread t3 = new Thread(costumer);
		Thread t4 = new Thread(costumer);
		t1.start();
		t2.start();
		t3.start();
		t4.start();
	}
}


class Producer implements Runnable {
	
	private Buffer buffer;
	private FileMock fileMock;
	public Producer(Buffer buffer,FileMock fileMock) {
		
		this.buffer = buffer;
		this.fileMock = fileMock;
	}
	@Override
	public void run() {
		
		String temp = null;
		buffer.setPending(true);
		while((temp=fileMock.getLine())!=null) {
			buffer.set(temp);			
		}
		buffer.setPending(false);
	}
}

class Costumer implements Runnable {
	
	private Buffer buffer;
	public Costumer(Buffer buffer) {
		
		this.buffer = buffer;
	}
	@Override
	public void run() {
		
		try {
			while(buffer.hasPending()) {
				buffer.get();
				Thread.sleep(500);
			}
		} catch (InterruptedException e) {
			throw new RuntimeException("the "+Thread.currentThread().getName()+" has been interrupted");
		}
	}
}


/**
 * @author 付大石
 * 缓冲区
 */
class Buffer {
	
	private Queue<String> buffer;
	private static final int MAX_SIZE = 5;
	private ReentrantLock lock;
	private Condition hasLine;
	private Condition hasSpace;
	private boolean pending;
	public Buffer() {
		
		buffer = new LinkedList<String>();
		lock = new ReentrantLock();
		hasLine = lock.newCondition();
		hasSpace = lock.newCondition();
		pending = false;
	}
	
	public void set(String line) {
		
		lock.lock();
		try {
			while(buffer.size()==MAX_SIZE) {
				hasSpace.await();
			}
			buffer.offer(line);
			System.out.printf("the %s set a line\n",Thread.currentThread().getName());
			hasLine.signalAll();
		} catch (InterruptedException e) {
			throw new RuntimeException("The "+Thread.currentThread().getName()+" has been interrupted",e);
		} finally {
			lock.unlock();
		}
	}
	
	public String get() {
		
		lock.lock();
		try {
			while(buffer.size()==0 && hasPending()) {
				hasLine.await();
			}
			if(hasPending()) {
				buffer.poll();
				System.out.printf("the %s get a line\n",Thread.currentThread().getName());
				hasSpace.signalAll();
			}
		} catch (InterruptedException e) {
			throw new RuntimeException("The "+Thread.currentThread().getName()+" has been interrutped",e);
		}
		lock.unlock();
		return null;
	}
	
	public int size() {
		
		return buffer.size();
	}
	
	public void setPending(boolean pending) {
		
		this.pending = pending;
	}
	
	public boolean hasPending() {
		
		return buffer.size()>0 || pending;
	}
}

/**
 * @author 付大石
 * 文件模拟类
 */
class FileMock {
	
	private String[] contentArr;
	private int cursor;
	private static final int LINE = 10;
	
	/**
	 * @param size
	 * 构造一个size大小的模拟文件
	 */
	public FileMock(int size) {
		
		contentArr = new String[size];
		
		/* 随机生成size条长度为LINE的字符串 */
		Random random = new Random();
		byte[] byteArr = null;
		for(int i=0;i<size;i++) {
			byteArr = new byte[LINE];
			for(int j=0;j<LINE;j++) {
				random.nextBytes(byteArr);
			}
			contentArr[i] = new String(byteArr);
		}
	}
	
	public boolean isEmpty() {
		
		return cursor>=contentArr.length;
	}
	
	public String getLine() {
		
		if(!isEmpty()) {
			return contentArr[cursor++];
		}
		return null;
	}
}