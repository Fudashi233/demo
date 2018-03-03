package test;

public class Test {
	
	public static void main(String[] args) {
		Singleton i1 = Singleton.INSTANCE;
		Singleton i2 = Singleton.INSTANCE;
		System.out.println(i1==i2);
	}
}


/**
 * @author 付大石
 * 饥汉-单例模式，简单实用，线程安全
 */
//class Singleton {
//	
//	private static Singleton instance = new Singleton();
//	
//	private Singleton() {
//		
//	}
//	
//	public static Singleton getInstance() {
//		return instance;
//	}
//}


/**
 * @author 付大石
 * 饱汉-单例模式，线程安全但并发程度不是很高
 */
//class Singleton {
//	
//	private static Singleton instance;
//	
//	private Singleton() {
//		
//	}
//	
//	public static synchronized Singleton getInstance() {
//		
//		if(instance==null) {
//			instance =new Singleton();
//		}
//		return instance;
//	}
//}


/**
 * @author 付大石
 * 饥汉-单例模式，线程安全，并发程度高于上一种，但比较复杂
 */
//class Singleton {
//	
//	private static Singleton instance;
//	
//	private Singleton() {
//		
//	}
//	
//	public static Singleton getInstance() {
//		
//		if(instance==null) {
//			synchronized(Singleton.class) {
//				if(instance==null) {
//					instance = new Singleton();
//				}
//			}
//		}
//		return instance;
//	}
//}


/**
 * @author 付大石
 * 即实现了延迟加载，性能由于无锁也不错
 */
//class Singleton {
//	
//	private static Singleton instance;
//	
//	private Singleton() {
//	}
//	
//	public static Singleton getInstance() {
//		return InstanceHolder.instance;
//	}
//	
//	private static class InstanceHolder {
//		private static Singleton instance = new Singleton();
//	}
//}


/**
 * 枚举
 */
//enum Singleton {  
//    INSTANCE;
//}  
