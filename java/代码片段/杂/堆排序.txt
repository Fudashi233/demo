import java.util.ArrayList;
public class Heap<T extends Comparable<T>>
{
	public ArrayList<T> list;
	public Heap()
	{
		list=new ArrayList<T>();
	}
	public Heap(T[] object)
	{
		list=new ArrayList<T>();
		for(int i=0;i<object.length;i++)
			this.add(object[i]);
	}
 	public void add(T t)
	{
		list.add(t);
		int currentIndex=list.size()-1;
		while(currentIndex>0)
		{
			int parentIndex=(currentIndex-1)/2;
			if( list.get(currentIndex).compareTo(list.get(parentIndex))>0 ) 
			{
			    T temp=list.get(currentIndex);
				list.set(currentIndex,list.get(parentIndex));
				list.set(parentIndex,temp);
			}
			else 
				break;
			currentIndex=parentIndex;
		} 
	}
 	public int getSize()
	{
		return list.size();
	} 
 	public T remove()
	{
		if(list.size()==0)
			return null;
		T result=list.get(0);
		list.set(0,list.get(list.size()-1));
		list.remove(list.size()-1);
		int currentIndex=0;
		while(currentIndex<list.size())
		{
			int leftIndex=currentIndex*2+1;
			int rightIndex=currentIndex*2+2;
			if(leftIndex>=list.size())
				break;
			int maxIndex=leftIndex;
			if(rightIndex<list.size())
				if(list.get(maxIndex).compareTo(list.get(rightIndex))<0)
                    maxIndex=rightIndex;
			if(list.get(maxIndex).compareTo(list.get(currentIndex))>0)
			{
				T temp=list.get(currentIndex);
				list.set(currentIndex,list.get(maxIndex));
				list.set(maxIndex,temp);
				currentIndex=maxIndex;
			}
			else 
				break;
		}
		return result;
	} 
 	public static void main(String[] args)
	{
		Integer[] a={2,3,7,4};
		Heap<Integer> heap=new Heap<Integer>(a);
		for(int i=0;i<a.length;i++)
			a[i]=heap.remove();
		for(int i=0;i<a.length;i++)
			System.out.print(a[i]);
	}
    	
}