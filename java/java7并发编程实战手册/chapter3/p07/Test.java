package chapter3.p07;

import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

public class Test {
	
	public static void main(String[] args) {
		
		Phaser phaser = new ExamPhaser();
		/* 创建5个Student */
		Runnable[] studentArr = new Student[5];
		for(int i=0;i<studentArr.length;i++) {
			studentArr[i] = new Student(phaser);
			phaser.register();
		}
		/* 创建5个线程 */
		Thread[] threadArr = new Thread[5];
		for(int i=0;i<threadArr.length;i++) {
			threadArr[i] = new Thread(studentArr[i]);
		}
		/* latch thread */
		for(int i=0;i<threadArr.length;i++) {
			threadArr[i].start();
		}
	}
}

class Student implements Runnable {
	
	private Phaser phaser;
	public Student(Phaser phaser) {
		this.phaser = phaser;
	}
	@Override
	public void run() {
		
		/* 0st phase:arrive */
		try {
			TimeUnit.SECONDS.sleep((int)(Math.random()*10));
		} catch (InterruptedException e) {
			throw new RuntimeException("the "+Thread.currentThread().getName()+" has been interrupted");
		}
		phaser.arriveAndAwaitAdvance();
		/* 1st phase:exercise1 */
		exercise1();
		phaser.arriveAndAwaitAdvance();
		/* 2st phase:exercise2 */
		exercise2();
		phaser.arriveAndAwaitAdvance();
		/* 3st phase:exercise3 */
		exercise3();
		phaser.arriveAndAwaitAdvance();
		/* 4st phase:finish */
		try {
			TimeUnit.SECONDS.sleep((int)(Math.random()*10));
		} catch (InterruptedException e) {
			throw new RuntimeException("the "+Thread.currentThread().getName()+" has been interrupted");
		}
		phaser.arriveAndAwaitAdvance();
	}
	
	private void exercise1() {
		doExercise();
	}
	
	private void exercise2() {
		doExercise();
	}
	
	private void exercise3() {
		doExercise();
	}
	
	private void doExercise() {
		
		try {
			int sleep = (int)(Math.random()*10);
			System.out.printf("the %s spend %d seconds\n",Thread.currentThread().getName(),sleep);
			TimeUnit.SECONDS.sleep(sleep);
		} catch (InterruptedException e) {
			throw new RuntimeException("the "+Thread.currentThread().getName()+" has been interrupted");
		}
	}
}

class ExamPhaser extends Phaser {
	
	@Override
	protected boolean onAdvance(int phase,int registerParties) {
		
		switch(phase) {
			case 0: {
				return studentArrive();
			} 
			case 1: {
				return exercise1();
			}
			case 2: {
				return exercise2();
			}
			case 3: {
				return exercise3();
			}
			default : {
				return true;
			}
		}
	}
	
	private boolean studentArrive() {
		
		System.out.printf("The students are ready,we have %d students\n",getRegisteredParties());
		return false;
	}
	
	private boolean exercise1() {
		
		System.out.println("finish the first exercise");
		return false;
	}
	
	private boolean exercise2() {
		
		System.out.println("finish the second exercise");
		return false;
	}
	
	private boolean exercise3() {
		
		System.out.println("finish the third exercise");
		return false;
	}
	
	private boolean finish() {
		
		System.out.println("finish exam");
		return true;
	}
}