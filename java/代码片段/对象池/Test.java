package test;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

/**
 * @author 付大石
 * 使用Semaphore构建一个对象池
 */
public class Test {
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException {
		
		Pool<Item> pool = new Pool<>(Item.class,10);
		Item item1 = pool.getItem();
		Item item2 = pool.getItem();
		System.out.println(item1);
		System.out.println(item2);
	}
	
	private static class Item {
		
		public Item() throws InterruptedException {
//			Thread.sleep(500);
		}
	}
	private static class Pool<T> {
		
		private ArrayList<T> items;
		private boolean[] check;
		private Semaphore semaphore;
		
		public Pool(Class<T> clazz,int size) throws InstantiationException, IllegalAccessException {
			
			
			check = new boolean[size];
			items = new ArrayList<>();
			for(int i=0;i<size;i++) {
				T instance = clazz.newInstance();
				items.add(instance);
			}
			semaphore = new Semaphore(size,true);
		}
		
		public T checkOut() throws InterruptedException {
			
			semaphore.acquire();
			return getItem();
		}
		
		private T getItem() {
			
			T item = null;
			for(int i=0;i<check.length;i++) {
				if(!check[i]) {
					item = items.get(i);
					check[i] = true;
					return item;
				}
			}
			return null;
		}
		
		public void checkIn(T item) {
			
			if(releaseItem(item)) {
				semaphore.release();
			}
		}
		
		private boolean releaseItem(T item) {
			
			int index = items.indexOf(item);
			if(index<0) { //pool中不存在该item,返回false
				return false;
			}
			if(check[index]) {
				check[index] = false;
				return true;
			}
			return false; //未被检出,返回false
		}
	}
}