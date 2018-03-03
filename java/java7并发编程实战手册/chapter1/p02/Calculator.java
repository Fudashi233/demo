package chapter1.p2;

/**
 * @author 付大石
 * 创建线程的方法
 * 1.继承Thread，覆盖run方法
 * 2.实现Runnable接口
 */
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
