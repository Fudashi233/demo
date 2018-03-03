package chapter1.p09;

import java.lang.Thread.UncaughtExceptionHandler;

/**
 * @author 付大石
 * 用于捕获和处理运行时异常
 */
public class Test {
	
	public static void main(String[] args) {
		
		Thread t = new Thread(new Task());
		t.setUncaughtExceptionHandler(new ExceptionHandler());
		t.start();
		
		//-----------------------------------------------------
		// 使用UncaughtExceptionHandler
		// Thread:Thread[Thread-0,5,main]
		// Throwable:java.lang.ArithmeticException: / by zero
		//------------------------------------------------------
		
		//----------------------------------
		// 未使用UncaughtExceptionHandler
		// Exception in thread "Thread-0" java.lang.ArithmeticException: / by zero
		// at chapter1.p9.Task.run(Test.java:25)
		// at java.lang.Thread.run(Thread.java:745)
		//----------------------------------
	}
}

class Task implements Runnable {

	@Override
	public void run() {
	
		int i = 5/0; //throw a runtime exception
	}
}

class ExceptionHandler implements UncaughtExceptionHandler{

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		
		System.out.println("Thread:"+t);
		System.out.println("Throwable:"+e);
	}
}
