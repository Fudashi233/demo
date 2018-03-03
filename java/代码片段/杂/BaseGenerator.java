//public class BaseGenerator
//{
//	private Class<?> type;
//	public BaseGenerator()
//	{
//		
//	}
//	public BaseGenerator(Class<?> type)
//	{
//		this.type = type;
//	}
//	public void setType(Class<?> type)
//	{
//		this.type = type;
//	}
//	public Object next()
//	{
//		try 
//		{
//			return type.newInstance();
//		} 
//		catch (InstantiationException e) 
//		{
//			e.printStackTrace();
//		} 
//		catch (IllegalAccessException e) 
//		{
//			e.printStackTrace();
//		}
//		return null;
//	}
//	public static void main(String[] args)
//	{
//		BaseGenerator generator = new BaseGenerator();
//		generator.setType(Coffee.class);
//		System.out.println(generator.next());
//		System.out.println(generator.next());
//		System.out.println(generator.next());
//		
//	}
//}




public class BaseGenerator<T>
{
	private Class<T> type;
	public BaseGenerator()
	{
		
	}
	public BaseGenerator(Class<T> type)
	{
		this.type = type;
	}
//	public void setType(Class<T> type)
//	{
//		this.type = type;
//	}
	public T next()
	{
		try 
		{
			return type.newInstance();
		} 
		catch (InstantiationException e) 
		{
			e.printStackTrace();
		} 
		catch (IllegalAccessException e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	public static void main(String[] args)
	{
		BaseGenerator<Coffee> generator = new BaseGenerator<Coffee>(Coffee.class);
		System.out.println(generator.next());
		System.out.println(generator.next());
		System.out.println(generator.next());
		
	}
}
