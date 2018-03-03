package chapter1.p3;

public class Calculator implements Runnable{
	
	private int num;
	public Calculator(int num) {
		
		this.num = num;
	}
	@Override
	public void run() {
		
		for(int i=0;i<10;i++) {
			System.out.printf("%s:%d*%d=%d\n",Thread.currentThread().getName(),num,i,num*i);
		}
	}
}

