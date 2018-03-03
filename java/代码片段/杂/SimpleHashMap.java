import java.util.AbstractMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class SimpleHashMap<K,V> extends AbstractMap<K,V>
{
	private static final int SIZE = 100;
	private LinkedList<Map.Entry<K,V>>[] map;
	@SuppressWarnings("unchecked")
	public SimpleHashMap()
	{
		map =  new LinkedList[SIZE];
	}
	@Override
	public Set<Map.Entry<K,V>> entrySet()
	{
		Set<Map.Entry<K,V>> entrySet = new LinkedHashSet<Map.Entry<K,V>>();
		for(LinkedList<Map.Entry<K,V>> list : map)
		{
			if(list==null) continue;
			for(Map.Entry<K,V> entry : list)
			{
				entrySet.add(entry);
			}
		}
		return entrySet;
	}
	@Override
	public V get(Object key)
	{
		int index = Math.abs(key.hashCode())%SIZE;
		LinkedList<Map.Entry<K,V>> list = map[index];
		if(list==null)
			return null;
		for(Map.Entry<K,V> entry : list)
		{
			if(key.equals(entry.getKey()))
			{
				return entry.getValue();
			}
		}
		return null;
	}
	@Override
	public V put(K key,V value)
	{
		V oldValue = get(key);
		int index = Math.abs(key.hashCode())%SIZE;
		if(map[index]==null)
			map[index] = new LinkedList<Map.Entry<K,V>>();
		LinkedList<Map.Entry<K,V>> list = map[index];
		boolean isFind = false;
		for(Map.Entry<K,V> entry : list)
		{
			if(key.equals(entry.getKey()))
			{
				list.set(list.indexOf(entry),new SimpleEntry<K,V>(key,value));
				isFind = true;
			}
		}
		//如果map中不存在该key，则添加一个新的entry
		if(!isFind)
		{
			System.out.println(key+"	"+value);
			list.add(new SimpleEntry<K,V>(key,value));
		}
		return oldValue;
	}
	public static void main(String[] args)
	{
		SimpleHashMap<Integer,String> map = new SimpleHashMap<Integer,String>();
		map.put(1,"a");
		map.put(1,"b");
		map.put(2,"c");
		map.put(3,"d");
		System.out.println(map);
	}
}
