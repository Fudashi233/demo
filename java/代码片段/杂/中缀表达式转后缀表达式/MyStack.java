import java.util.ArrayList;
public class MyStack<T>
{
    public ArrayList<T> list;
    public int size;	
	public MyStack()
	{
		list=new ArrayList<T>();
		size=0;
	}
	public int getSize()
	{
		return this.size;
	}
	public T peek()
	{
		if(this.size==0)
			return null; 
		return list.get(size-1);
	}
	public void push(T t)
	{
		list.add(t);
		this.size++;
	}
	public T pop()
	{
        T t=peek();
		list.remove(size-1);
		this.size--;
		return t;
	}
	public void clear()
	{
		list=null;
		size=0;
	}
	public boolean isEmpty()
	{
		return this.size==0;
	}
}