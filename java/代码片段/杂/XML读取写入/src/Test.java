public class Test
{
	public static void main(String[] args)
	{
		A a = new A();
		A b = new A();
		a.a=5;
		b.a=2;
		f(a,b);
		System.out.println(a.a+"---"+b.a);
	}
	private static void f(A a,A b)
	{
		A c = new A();
		c.a = a.a;
		a.a = b.a;
		b.a = c.a;
	}
}
class A
{
	public int  a;
}