package utils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class BeanUtil {

	public static Map<String,Object> beanToMap(Object obj) throws IllegalArgumentException, IllegalAccessException {
		
		if(obj==null) {
			
			return null;
		}
		Field[] fieldArr = obj.getClass().getFields();
		Map<String,Object> map = new HashMap<String,Object>();
		for(int i=0;i<fieldArr.length;i++) {
			
			map.put(fieldArr[i].getName(),fieldArr[i].get(obj));
		}
		return map;
	}
	
	/**
	 * 利用内省机制为bean赋值
	 * 
	 * @param paramMap
	 * @param clazz
	 * @return
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 */
	public static Object getBean(Map<String, Object> paramMap,Class<?> clazz) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, SecurityException {
		
		Object bean = clazz.newInstance();
		Set<Entry<String,Object>> set = paramMap.entrySet();
		Iterator<Entry<String,Object>> iterator = set.iterator();
		while(iterator.hasNext()) {
			
			Entry<String,Object> entry = iterator.next();
			String fieldName = entry.getKey();
			Object fieldValue = entry.getValue();
			System.out.println("52---"+fieldName+"	"+fieldValue);
			try {
				
				PropertyDescriptor PD = new PropertyDescriptor(fieldName,clazz);
				Method writer = PD.getWriteMethod();
				writer.invoke(bean,fieldValue);
			} catch (IntrospectionException e) {
				
				e.printStackTrace();//map中有某些数据可能不是bean的数据域，此时就会抛出这个异常，无视就好
			}
		}
		return bean;
	}
	
	public static Object getBean(Class<?> clazz) throws InstantiationException, IllegalAccessException {
		
		return clazz.newInstance();
	}
	
	public static Object getBean(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		
		if(className!=null && !"".equals(className.trim())) {
			
			Class<?> clazz = Class.forName(className);
			return getBean(clazz);
		} else {
			
			throw new IllegalArgumentException("参数className不能为null或空串");
		}
	}
}
