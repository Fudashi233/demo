package chapter1.p06;

public class Test {
	
	public static void main(String[] args) {
		
		Thread t = new Thread(new Clock());
		t.start();
		t.start();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		t.interrupt();
	}
}
