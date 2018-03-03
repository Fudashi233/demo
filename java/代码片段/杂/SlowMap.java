import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class SlowMap<K,V> extends AbstractMap<K,V>
{
	public static void main(String[] args)
	{
		SlowMap<Integer,String> map= new SlowMap<Integer,String>();
		map.put(1,"a");
		map.put(2,"b");
		map.put(3,"c");
		System.out.println(map);
	}
	private ArrayList<K> keyList;
	private ArrayList<V> valueList;
	public SlowMap()
	{
		keyList = new ArrayList<K>();
		valueList = new ArrayList<V>();
	}
	public Set<Map.Entry<K,V>> entrySet()
	{
		Set<Map.Entry<K,V>> entrySet = new LinkedHashSet<Map.Entry<K,V>>();
		Iterator<K> keyIterator = keyList.iterator();
		Iterator<V> valueIterator = valueList.iterator();
		while(keyIterator.hasNext())
		{
			entrySet.add(new SimpleEntry<K,V>(keyIterator.next(),valueIterator.next()));
		}
		return entrySet;
	}
	//1.map中不存在的，直接添加，返回null
	//2.map中存在的，覆盖old值，返回old值
	public V put(K key,V value)
	{
		V oldValue = get(key);
		if(keyList.contains(key))
		{
			int index = keyList.indexOf(key);
			valueList.set(index,value);
		}
		else
		{
			keyList.add(key);
			valueList.add(value);
		}
		return oldValue;
	}
	public V get(Object key)
	{
		int index = keyList.indexOf(key);
		if(index<0)
			return null;
		return valueList.get(index);
	}
}

