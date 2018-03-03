package chapter1.p2;

public class Test {
	
	public static void main(String[] args) {
		
		/*创建10个线程*/
		for(int i=0;i<10;i++) {
			Thread t = new Thread(new Calculator(i));
			t.start();
		}
	}
}
