package chapter1.p4;

public class PrimeGenerator extends Thread{

	@Override
	public void run() {
		
		long num = 1L;
		while(true) {
			if(isPrime(num)) {
				System.out.println("prime number"+num+"		"+isInterrupted());
			}
			if(isInterrupted()) {
				System.out.println("The thread has been interrupted");
				return ;
			}
			num++;
		}
		
	}
	
	/**
	 * 判断是否是质数
	 * @param num
	 * @return
	 */
	private boolean isPrime(long num) {
		
		if(num<=0) {
			throw new IllegalArgumentException("参数num是负数");
		}
		if(num<=2) {
			return true;
		}
		for(long i=2;i<num;i++) {
			if(num%i==0) {
				return false;
			}
		}
		return true;
	}
}
