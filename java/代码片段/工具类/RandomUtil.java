package utils;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RandomUtil {
	
	public static final long MIN = 10;
	public static final long MAX = 15;
	public static final String RANDOM_STR = "abcdef";
	public static final int PRECISION = 2;
	public static final int STR_LENGTH = 10;
	public static final boolean STR_LEN_RANDOM = true;//string length random，字符串长度是否随机
	public static final int ARR_LENGTH = 10;//数组最大长度
	public static final boolean ARR_LEN_RANDOM = true;//array length random，数组长度是否随机
	public static final int COLLECTION_SIZE = 5;//集合最大大小
	public static final boolean COL_SIZE_RANDOM = true;//collection size random，集合的size是否随机
	public static final int MAP_SIZE = 5;//映射最大大小
	public static final boolean MAP_SIZE_RANDOM = true;//map size random，映射的size是否随机
	
	public static Integer getRandomInteger() {
		
		return getRandomInteger((int)MIN,(int)MAX);
	}
	/**
	 * 随机生成一个整型
	 * @param min
	 * @param max
	 * @return
	 */
	public static Integer getRandomInteger(int min,int max) {
		
		if(min>max) {
			
			throw new IllegalArgumentException("参数异常：min需要小于等于max");
		} else {
			
			return (int)((Math.random()*(max-min))+min);
		}
	}
	
	public static Long getRandomLong() {
		
		return getRandomLong(MIN,MAX);
	}
	public static Long getRandomLong(long min,long max) {
		
		if(min>max) {
			
			throw new IllegalArgumentException("参数异常：min需要小于等于max");
		} else {
			
			return (long)((Math.random()*(max-min))+min);
		}
	}
	public static Byte getRandomByte() {
		
		return getRandomByte((byte)MIN,(byte)MAX);
	}
	public static Byte getRandomByte(byte min,byte max) {
		
		if(min>max) {
			
			throw new IllegalArgumentException("参数异常：min需要小于等于max");
		} else {
			
			return (byte)((Math.random()*(max-min))+min);
		}
	}
	public static Character getRandomChar() {
		
		return getRandomChar(RANDOM_STR);
	}
	/**
	 * 随机生成一个字符
	 * @param str
	 * @return
	 */
	public static Character getRandomChar(String str) {
		
		int index = (int)(getRandomInteger(0,str.length()).longValue());
		return str.charAt(index);
	}
	public static String getRandomString() {
		
		return getRandomString(STR_LENGTH,RANDOM_STR,STR_LEN_RANDOM);
	}
	/**
	 * 随机生成一个字符串
	 * @param len 长度
	 * @param str 从str中选取字符
	 * @param randomLen	长度是否随机
	 * @return
	 */
	public static String getRandomString(int len,String str,boolean strLenRandom) {
		
		StringBuilder stringBuilder = new StringBuilder();
		len = strLenRandom?(int)(getRandomInteger(0,len).longValue()):len;
		for(int i=0;i<len;i++) {
			
			stringBuilder.append(getRandomChar(str));
		}
		return stringBuilder.toString();
	}
	public static Double getRandomDouble() {
		
		return getRandomDouble(MIN,MAX,PRECISION);
	}
	/**
	 * 获取范围在min和max之间的一个双精度浮点数，精度是precision
	 * @param min
	 * @param max
	 * @param precision 精度
	 * @return
	 */
	public static Double getRandomDouble(long min,long max,int precision) {
		
		if(min>max) {
			
			throw new IllegalArgumentException("参数异常：min需要小于等于max");
		} else {
			
			double result = (Math.random()*(max-min))+min;
			BigDecimal bigDecimal = new BigDecimal(result);
			return bigDecimal.setScale(precision,BigDecimal.ROUND_HALF_UP).doubleValue();//根据PRECISION获取精度
		} 
	}
	public static Float getRandomFloat() {
		
		return getRandomFloat(MIN,MAX,PRECISION);
	}
	public static Float getRandomFloat(long min,long max,int precision) {
		
		if(min>max) {
			
			throw new IllegalArgumentException("参数异常：min需要小于等于max");
		} else {
			
			double result = (Math.random()*(max-min))+min;
			BigDecimal bigDecimal = new BigDecimal(result);
			return (float)bigDecimal.setScale(precision,BigDecimal.ROUND_HALF_UP).doubleValue();//根据PRECISION获取精度
		} 
	}

	public static Boolean getRandomBoolean() {
		
		return new Random().nextBoolean();
	}
	public static <T> Object getRandomArray(Class<T> clazz) throws ArrayIndexOutOfBoundsException, IllegalArgumentException, IllegalAccessException, InstantiationException {
		
		return getRandomArray(clazz,ARR_LENGTH,ARR_LEN_RANDOM);
	}
	public static <T> Object getRandomArray(Class<T> clazz,int len,boolean arrLenRandom) throws ArrayIndexOutOfBoundsException, IllegalArgumentException, IllegalAccessException, InstantiationException{
		
		len = arrLenRandom?(int)(getRandomInteger(0,len).longValue()):len;
		LogUtil.info(clazz.toString());
		Object array = Array.newInstance(clazz,len);
		if(isString(clazz)) { //String特殊处理 ,getRandomInstance无法实例化String
			for(int i=0;i<len;i++) {
				
				Array.set(array,i,getRandomString());
			}
		} else {
			for(int i=0;i<len;i++) {
				
				Array.set(array,i,getRandomInstance(clazz));
			}
		}
		return array;
	}
	
	public static <T> Collection<T> getRandomCollection(Collection collection,Class<T> elementClass) throws IllegalArgumentException, IllegalAccessException, InstantiationException, InvocationTargetException {
		
		return getRandomCollection(collection,elementClass,COLLECTION_SIZE,COL_SIZE_RANDOM);
	}
	public static <T> Collection<T> getRandomCollection(Collection<T> collection,Class<T> elementClass,int size,boolean sizeRandom) throws IllegalArgumentException, IllegalAccessException, InstantiationException, InvocationTargetException {
		
		if(collection==null) {//默认collection的实际类型为ArrayList
			
			collection = new ArrayList<T>();
		}
		size = sizeRandom?(int)getRandomInteger(0,size):size;
		if(RandomUtil.isString(elementClass)) { //String特殊处理 ,getRandomInstance无法实例化String
			for(int i=0;i<size;i++) {
				collection.add((T)getRandomString());
			}
		} else {
			for(int i=0;i<size;i++) {
				
				collection.add(getRandomInstance(elementClass));
			}
		}
		return collection;
	}
	
	
	public static Map<?,?> getRandomMap(Class<?> keyClass,Class<?> valueClass) throws IllegalArgumentException, IllegalAccessException, InstantiationException {
		
		return getRandomMap(null,keyClass,valueClass,MAP_SIZE,MAP_SIZE_RANDOM);
	}

	public static <K,V> Map<K,V> getRandomMap(Map<K,V> map,Class<?> keyClass,Class<?> valueClass,int size,boolean sizeRandom) throws IllegalArgumentException, IllegalAccessException, InstantiationException {
		
		size = sizeRandom?(int)getRandomInteger(0,size):size;
		K key = null;
		V value = null;
		if(map==null)
			map = new HashMap<K,V>();
		for(int i=0;i<size;i++) {
			if(RandomUtil.isString(keyClass)) {

				key = (K)getRandomString();
			} else {
				
				key = (K)getRandomInstance(keyClass);
			}
			if(RandomUtil.isString(valueClass)) {

				value = (V)getRandomString();
			} else {
				
				value = (V)getRandomInstance(valueClass);
			}
			map.put(key,value);
		}
		return map;
	}
	
	/**
	 * 
	 * @param clazz	字节码对象
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws InstantiationException 
	 * @throws InvocationTargetException 
	 * @return 返回一个数据域是随机生成的类
	 */
	public static <T> T getRandomInstance(Class<T> clazz) throws IllegalArgumentException, 
									IllegalAccessException, InstantiationException {
		
		//判断是否存在默认构造函数
		Constructor<?>[] constructorArr = clazz.getConstructors();
		T instance = null;
		for(int i=0;i<constructorArr.length;i++) {
			if(constructorArr[i].getParameterTypes().length==0) {
				
				instance = clazz.newInstance();
			}
		}
		if(instance == null) {//如果目标类无默认构造函数
			if(isBaseType(clazz)) {
				
				return getBaseValue(clazz);
			} else {
				
				throw new IllegalArgumentException(clazz.getSimpleName()+"目标类必须要有默认构造函数");
			}
		}
		//获取clazz中的数据域
		Field[] fieldArr = clazz.getDeclaredFields();
		//为各个数据域赋值
		for(int i=0;i<fieldArr.length;i++) {
			
			Class<?> fieldType = fieldArr[i].getType();
			fieldArr[i].setAccessible(true);
			
			if(Modifier.isFinal(fieldArr[i].getModifiers()) || Modifier.isStatic(fieldArr[i].getModifiers())){
				
				continue;
			}
				
			if(fieldType.isArray()) {	//数组特殊处理
				
				fieldArr[i].set(instance,getRandomArray(fieldType.getComponentType()));
			} else {

				if(isBaseType(fieldType)) {//基本类型特殊处理
					
					fieldArr[i].set(instance,RandomUtil.getBaseValue(fieldType));
				} else {
					
					Object fieldInstance = null;
					try {
						
						fieldInstance = fieldType.newInstance();
					} catch (InstantiationException ex) {
						
						throw new IllegalArgumentException(clazz.getSimpleName()+"中的"+fieldArr[i].getName()+"属性，无默认构造函数，无法构造");
					}
					
					if(isString(fieldType)) {
						
						fieldArr[i].set(instance,getRandomString());
					} else if(Collection.class.isInstance(fieldInstance)) {
						
						//TODO
					} else if(Map.class.isInstance(fieldInstance)) {
						
						//TODO
					} else {
						
						fieldArr[i].set(instance,getRandomInstance(fieldType));
					}
				}
			}
		}
		return instance;
	}
	/**
	 * 由于包装类都不具有默认构造函数，实际应用中又常常需要用到
	 * 所以特地写一个函数，已随机生成基本类型值
	 * @param clazz
	 * @return
	 */
	private static <T> T getBaseValue(Class<T> clazz) {
		
		if(isByte(clazz)) {
			
			return (T)getRandomByte();
		} else if(isInteger(clazz)) {
			return (T)getRandomInteger();
		} else if(isLong(clazz)) {
			return (T)getRandomLong();
		} else if(isDouble(clazz)) {
			return (T)getRandomDouble();
		} else if(isFloat(clazz)) {
			return (T)getRandomFloat();
		} else if(isBoolean(clazz)) {
			return (T)getRandomBoolean();
		} else if(isCharacter(clazz)) {
			return (T)getRandomChar();
		}
		throw new IllegalArgumentException("clazz应是基本类型的字节码对象");
	}
	private static boolean isBaseType(Class<?> type) {
		
		if(isInteger(type)) {//如果是整型
			
			return true;
		}
		if(isLong(type)) {//如果是长整形
			
			return true;
		}
		if(isByte(type)) {//如果是比特型
			
			return true;
		}
		if(isCharacter(type)) {//如果是字符型
			
			return true;
		}
		if(isDouble(type)) {//如果双精度浮点型
			
			return true;
		}
		if(isFloat(type)) {//如果是单精度浮点型
			
			return true;
		}
		if(isBoolean(type)) {//如果是布尔型
	
			return true;
		}
		return false;
	}
	private static boolean isInteger (Class<?> type) {
		
		if(type.equals(Integer.class)) { //判断是否是包装类
			
			return true;
		}
		if(type.equals(Integer.TYPE)) { //判断是否是基础类型
			
			return true;
		}
		return false;
	}

	private static boolean isLong(Class<?> type) {
		
		if(type.equals(Long.class)) { //判断是否是包装类
			
			return true;
		}
		if(type.equals(Long.TYPE)) { //判断是否是基础类型
			
			return true;
		}
		return false;
	}
	
	private static boolean isByte(Class<?> type) {
		
		if(type.equals(Byte.class)) { //判断是否是包装类
			
			return true;
		}
		if(type.equals(Byte.TYPE)) { //判断是否是基础类型
			
			return true;
		}
		return false;
	}
	
	private static boolean isCharacter(Class<?> type) {
		
		if(type.equals(Character.class)) {	//判断是否是包装类
			
			return true;
		}
		if(type.equals(Character.TYPE)) { //判断是否是基础类型
			
			return true;
		}
		return false;
	}
	
	private static boolean isDouble(Class<?> type) {
		
		if(type.equals(Double.class)) {
			
			return true;
		}
		if(type.equals(Double.TYPE)) {
			
			return true;
		}
		return false;
	}
	
	private static boolean isFloat(Class<?> type) {
		
		if(type.equals(Float.class)) {
			
			return true;
		}
		if(type.equals(Float.TYPE)) {
			
			return true;
		}
		return false;
	}
	
	private static boolean isString(Class<?> type) {
		
		if(type.equals(String.class)) {
			
			return true;
		}
		return false;
	}
	
	private static boolean isBoolean(Class<?> type) {
		
		if(type.equals(Boolean.class)) {
			
			return true;
		}
		if(type.equals(Boolean.TYPE)) {
			
			return true;
		}
		return false;
	}
}
