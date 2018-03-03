package cn.edu.jxau.test;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCache<K, V> extends LinkedHashMap<K, V> {

	public static void main(String[] args) {
		LRUCache<Integer,String> cache = new LRUCache<>(3);
		cache.put(1,"a");
		cache.put(2,"b");
		cache.put(3,"c");
		cache.put(4,"d");
		cache.put(5,"e");
		cache.put(6,"f");
		cache.put(7,"g");
		System.out.println(cache.size());
	}
	
	private static final int DEFAULT_CAPACITY = 1024;
	private int capacity;

	public LRUCache() {
		this(DEFAULT_CAPACITY);
	}

	public LRUCache(int capacity) {
		super(capacity * 2, 0.75f, true);
		this.capacity = capacity;
	}

	@Override
	protected boolean removeEldestEntry(Map.Entry<K, V> entry) {

		if (this.size() > capacity) {
			System.out.printf("LRUCache clean:[%s,%s]\n", entry.getKey(), entry.getValue());
			return true;
		}
		return false;
	}
}
