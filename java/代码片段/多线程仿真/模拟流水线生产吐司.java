package test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author ����ʯ
 * ��˾block queue��
 * ��������������һ��������˾��һ��Ĩ���ͣ�һ��Ĩ������
 * ʹ��BlockQueue��������ˮ��
 */
public class Test {
	
	private enum Status {DRY,BUTTERED,JAMMED};
	
	public static void main(String[] args) throws InterruptedException {
		
		BlockingQueue<Toast> buttered = new LinkedBlockingQueue<>();
		BlockingQueue<Toast> jammed = new LinkedBlockingQueue<>();
		BlockingQueue<Toast> eater = new LinkedBlockingQueue<>();
		ExecutorService executor = Executors.newCachedThreadPool();
		executor.execute(new Toaster(buttered));
		executor.execute(new Buttered(buttered,jammed));
		executor.execute(new Jammed(jammed,eater));
		executor.execute(new Eater(eater));
		executor.shutdown();
		executor.awaitTermination(1,TimeUnit.HOURS);
		System.out.println("all is over");
	}
	
	private static class Toast {
		private Status status = Status.DRY;
		private final int id;
		public Toast(int id) {
			this.id = id;
		}
		public void buttered() {
			status = Status.BUTTERED;
		}
		public void jammed() {
			status = Status.JAMMED;
		}
		public int getID() {
			return id;
		}
		public Status getStatus() {
			return status;
		}
		@Override
		public String toString() {
			return String.format("toast:%d",id);
		}
	}
	
	/**
	 * @author ����ʯ
	 * ����toaster
	 */
	private static class Toaster implements Runnable {
		
		private BlockingQueue<Toast> buttered;
		
		private Toaster(BlockingQueue<Toast> buttered) {
			this.buttered = buttered;
		}
		
		@Override
		public void run() {
			
			try {
				for(int i=0;i<20;i++) {
					ThreadUtil.randomSleep(1000);
					buttered.put(new Toast(i)); //����Toast���󣬷���buttered���������
				}
			} catch (InterruptedException e) {
				throw new RuntimeException("the "+ThreadUtil.currentThreadName()+" has been interrupted");
			}
		}
	}
	
	/**
	 * @author ����ʯ
	 * ��buttered������ȡ��Toast���󣬽��ж�Ӧ�ļӹ�������
	 * Ȼ�����jammed����
	 */
	private static class Buttered implements Runnable {
		
		private BlockingQueue<Toast> buttered;
		private BlockingQueue<Toast> jammed;
		public Buttered(BlockingQueue<Toast> buttered,BlockingQueue<Toast> jammed) {
			
			this.buttered = buttered;
			this.jammed = jammed;
		}
		
		@Override
		public void run() {
			
			Toast t = null;
			try {
				while((t=buttered.poll(5,TimeUnit.SECONDS))!=null) {
					t.buttered();
					jammed.put(t); //����jammed���������
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("buttered is over");
		}
	}
	
	/**
	 * @author ����ʯ
	 * ��jammed������ȡ��Toast���󲢴���Ȼ�����eater������
	 */
	public static class Jammed implements Runnable {
		
		private BlockingQueue<Toast> jammed;
		private BlockingQueue<Toast> eater;
		
		public Jammed(BlockingQueue<Toast> jammed,BlockingQueue<Toast> eater) {
			
			this.jammed = jammed;
			this.eater = eater;
		}
		
		@Override
		public void run() {
			
			Toast t = null;
			try {
				while((t = jammed.poll(5,TimeUnit.SECONDS))!=null) {
					t.jammed();
					eater.put(t); //����eater�ȴ�������
				}
			} catch (InterruptedException e) {
				throw new RuntimeException("the "+ThreadUtil.currentThreadName()+" has been interrutped",e);
			}
			System.out.println("jammed is over");
		}
	}
	
	public static class Eater implements Runnable {
		
		private BlockingQueue<Toast> eater;
		public Eater(BlockingQueue<Toast> eater) {
			this.eater = eater;
		}
		
		@Override
		public void run() {
			
			Toast t = null;
			try {
				while((t = eater.poll(5,TimeUnit.SECONDS))!=null) {
					System.out.println(t);
				}
			} catch (InterruptedException e) {
				throw new RuntimeException("the "+ThreadUtil.currentThreadName()+" has been interrupted");
			}
			System.out.println("eater is over");
		}
	}
}