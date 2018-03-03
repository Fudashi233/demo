public class Fibonacci implements Generator<Integer>
{
	int count;
	public Fibonacci()
	{
		count = 0;
	}
	@Override
	public Integer next()
	{
		count++;
		return fib(count);
	}
	private int fib(int n)
	{
		if(n<=2)
			return 1;
		return fib(n-2)+fib(n-1);
	}
	public static void main(String[] args)
	{
		Fibonacci f = new Fibonacci();
		for(int i=0;i<100;i++)
		{
			System.out.println(f.next());
		}
	}
}
