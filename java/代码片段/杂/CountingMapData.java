import java.util.AbstractMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.Map;

public class CountingMapData extends AbstractMap<Integer,String>
{
	private static char[] charArr = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N'};
	private int size;
	
	public CountingMapData()
	{
		this(1);
	}
	
	public CountingMapData(int size)
	{
		this.size = size;
	}
	
	private class MyEntry implements Map.Entry<Integer,String>
	{
		private int index;
		
		public MyEntry()
		{
			this(1);
		}
		
		public MyEntry(int index)
		{
			this.index = index;
		}
		
		@Override
		public boolean equals(Object obj)
		{
			return obj instanceof MyEntry
						&&	((MyEntry)obj).getKey()==index;
		}
		
		@Override
		public Integer getKey()
		{
			return index;
		}

		@Override
		public String getValue()
		{
			return CountingMapData.charArr[index%CountingMapData.charArr.length]+Integer.toString(index);
		}
		
		@Override
		public String setValue(String value)
		{
			throw new UnsupportedOperationException();
		}
		
	}
	public Set<Map.Entry<Integer, String>> entrySet()
	{
		Set<Map.Entry<Integer,String>> entrySet = new LinkedHashSet<Map.Entry<Integer,String>>();
		for(int i=0;i<size;i++)
		{
			entrySet.add(new MyEntry(i));
		}
		return entrySet;
	}
	
	public static void main(String[] args)
	{
		CountingMapData map = new CountingMapData(20);
		map.put(1,"s");
	}
}
