import java.util.*;

public class Test 
{
	public static void main(String[] args)
	{
		FruitGenerator generator = new FruitGenerator();
		for(int i=0;i<5;i++)
			System.out.println(generator.next());
		f(Fruit.class);
	}
	public static void f(Class<? super Apple> w)
	{
		
	}
} 
class FruitGenerator implements Generator<Fruit>,Iterable<Fruit>
{
	private ArrayList<Class<? extends Fruit>> types;
	private int size;
	
	public FruitGenerator()
	{
		types = new ArrayList<Class<? extends Fruit>>();
		types.add(Fruit.class);
		types.add(Apple.class);
		types.add(Orange.class);
		types.add(Jonathan.class);
		size = 0;
	}
	
	public FruitGenerator(int size)
	{
		types = new ArrayList<Class<? extends Fruit>>();
		types.add(Fruit.class);
		types.add(Apple.class);
		types.add(Orange.class);
		types.add(Jonathan.class);
		this.size = size;
	}
	
	private class FruitIterator implements Iterator<Fruit>
	{
		@Override
		public boolean hasNext()
		{
			return size!=0;
		}

		@Override
		public Fruit next()
		{
			size--;
			return FruitGenerator.this.next();
		}
		
		@Override
		public void remove()
		{
			throw new UnsupportedOperationException();
		}
	}
	
	@Override
	public Iterator<Fruit> iterator() 
	{
		return new FruitIterator();
	}
	
	@Override
	public Fruit next() 
	{
		int index = (int)(Math.random()*types.size());
		try 
		{
			return types.get(index).newInstance();
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
}
class Fruit
{
	
}
class Apple extends Fruit
{
	
}
class Orange extends Fruit
{
	
}
class Jonathan extends Apple
{
	
}