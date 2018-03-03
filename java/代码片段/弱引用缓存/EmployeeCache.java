package test;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author 付大石
 * Employee缓存
 */
public class EmployeeCache {
	
	public static void main(String[] args) {
		
		long start = System.nanoTime();
		Employee employee = EmployeeCache.getInstance().getEmployee(1);
		long end = System.nanoTime();
		System.out.println(end-start);
		start = System.nanoTime();
		EmployeeCache.getInstance().getEmployee(1);
		end = System.nanoTime();
		System.out.println((end-start));
	}
	
	private static EmployeeCache instance = new EmployeeCache();
	private ReferenceQueue<Employee> queue; //无用的软引用队列
	private Map<Integer,EmployeeRef> refMap; //id与相应软引用的映射集
	
	
	/**
	 * @author 付大石
	 * 指向Employee的soft reference
	 */
	private static class EmployeeRef extends SoftReference<Employee> {
		
		private int key;
		public EmployeeRef(Employee employee, ReferenceQueue<Employee> queue) {
			super(employee,queue);
			this.key = employee.getId();
		}
	}
	
	/**
	 * 为实现单例模式，私有化构造函数
	 */
	private EmployeeCache() {
		
		refMap = new ConcurrentHashMap<>();
		queue = new ReferenceQueue<Employee>();
	}
	
	public static EmployeeCache getInstance() {
		return instance;
	}
	
	private void cacheEmployee(Employee em) {
		
		cleanCache();
		EmployeeRef employeeRef = new EmployeeRef(em,queue);
		refMap.put(em.getId(),employeeRef);
	}
	
	private void cleanCache() {
		
		EmployeeRef ref = null;
		while((ref=(EmployeeRef) queue.poll())!=null) {
			refMap.remove(ref.key);
		}
		Employee employee = new Employee();
	}
	
	/**
	 * @param ID
	 * @return
	 * 
	 * 获取Employee对象，先尝试从缓存中获取，如果无法获取再使用常规方法获取
	 */
	public Employee getEmployee(int ID) {
		
		EmployeeRef employeeRef = refMap.get(ID);
		Employee employee = null;
		if(employeeRef!=null) { //成功从缓存中获取
			employee = employeeRef.get();
		} else {
			employee = new Employee();
			employee.setId(ID);
			cacheEmployee(employee);
		}
		return employee;
	}
}


