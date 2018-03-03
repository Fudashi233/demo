package random;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class RandomUtils {

    /**
     * 随机生成的整数所在范围是[MIN_XXX,MAX_XXX)
     */
    private static final int MIN_LONG = 1;
    private static final int MAX_LONG = 10;

    private static final int MIN_INT = 1;
    private static final int MAX_INT = 10;

    private static final byte MIN_BYTE = 1;
    private static final byte MAX_BYTE = 10;

    private static final short MIN_SHORT = 1;
    private static final short MAX_SHORT = 10;

    private static final double MIN_DOUBLE = 1;
    private static final double MAX_DOUBLE = 10;

    private static final float MIN_FLOAT = 1;
    private static final float MAX_FLOAT = 10;

    private static final char[] CHAR_ARR = { '1', 'a', 'v', 'd', 's' };
    private static final char[] NUMBER_CHAR_ARR = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
    private static final char[] LETTER_CHAR_ARR = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
            'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

    private static final int STR_MIN_LEN = 7;
    private static final int STR_MAX_LEN = 7;
    
    private static final int ARR_MIN_LEN = 4;
    private static final int ARR_MAX_LEN = 4;
    
    private static final int COLL_MIN_SIZE = 5;
    private static final int COLL_MAX_SIZE = 5;
    
    private static final int MAP_MIN_SIZE = 5;
    private static final int MAP_MAX_SIZE = 5;
    /**
     * 私有的默认构造函数，禁止实例化
     */
    private RandomUtils() {

    }

    // -------------------------------------
    // 随机生成基本类型
    // -------------------------------------

    /**
     * @param min
     * @param max
     * @return
     * 生成一个在[min,max)域内的long类型的数据
     */
    public static long randomLong(long min, long max) {

        if (min > max) {
            throw new IllegalArgumentException(String.format("min=%d,max=%d，min应当小于等于max", min, max));
        }
        return (long) (Math.random() * (max - min) + min);
    }

    public static long randomLong() {
        return randomLong(MIN_LONG, MAX_LONG);
    }

    public static long randomLongWithMin(long min) {
        return randomLong(min, Long.MAX_VALUE);
    }

    public static long randomLongWithMax(long max) {
        return randomLong(Long.MIN_VALUE, max);
    }

    /**
     * @param min
     * @param max
     * @return
     * 生成一个在[min,max)域内的int类型的数据
     */
    public static int randomInt(int min, int max) {
        return (int) randomLong(min, max);
    }

    public static int randomInt() {
        return randomInt(MIN_INT, MAX_INT);
    }

    public static int randomIntWithMin(int min) {
        return randomInt(min, Integer.MAX_VALUE);
    }

    public static int randomIntWithMax(int max) {
        return randomInt(Integer.MIN_VALUE, max);
    }

    /**
     * @param min
     * @param max
     * @return
     * 生成一个在[min,max)域内的byte类型的数据
     */
    public static byte randomByte(byte min, byte max) {
        return (byte) randomLong(min, max);
    }

    public static byte randomByte() {
        return randomByte(MIN_BYTE, MAX_BYTE);
    }

    public static byte randomByteWithMin(byte min) {
        return randomByte(min, Byte.MAX_VALUE);
    }

    public static byte randomByteWithMax(byte max) {
        return randomByte(Byte.MIN_VALUE, max);
    }

    /**
     * @param min
     * @param max
     * @return
     * 生成一个在[min,max)域内的byte类型的数据
     */
    public static short randomShort(short min, short max) {
        return (short) randomLong(min, max);
    }

    public static short randomShort() {
        return randomShort(MIN_SHORT, MAX_SHORT);
    }

    public static short randomShortWithMin(short min) {
        return randomShort(min, Short.MAX_VALUE);
    }

    public static short randomShortWithMax(short max) {
        return randomShort(Short.MIN_VALUE, max);
    }

    /**
    * @param min
    * @param max
    * @return
    * 生成一个在[min,max)域内的double类型的数据
    */
    public static double randomDouble(double min, double max) {

        if (min > max) {
            throw new IllegalArgumentException(String.format("min=%f,max=%f，min应当小于等于max", min, max));
        }
        return Math.random() * (max - min) + min;
    }

    public static double randomDouble() {
        return randomDouble(MIN_DOUBLE, MAX_DOUBLE);
    }

    public static double randomDoubleWithMin(double min) {
        return randomDouble(min, Double.MAX_VALUE);
    }

    public static double randomDoubleWithMax(double max) {
        return randomDouble(Double.MIN_VALUE, max);
    }

    /**
     * @param min
     * @param max
     * @return
     * 生成一个在[min,max)域内的float类型的数据
     */
    public static float randomFloat(float min, float max) {
        return (float) randomDouble(min, max); // 调用double类型的随机数生成器
    }

    public static float randomFloat() {
        return randomFloat(MIN_FLOAT, MAX_FLOAT);
    }

    public static float randomDoubleWithMin(float min) {
        return randomFloat(min, Float.MAX_VALUE);
    }

    public static float randomDoubleWithMax(float max) {
        return randomFloat(Float.MIN_VALUE, max);
    }

    /**
     * @return
     * 随机生成一个布尔值，true和false的概率各50%
     */
    public static boolean randomBoolean() {

        long i = randomLong(0, 2);
        if (i == 0) {
            return false;
        } else {
            return true;
        }
    }

    // TODO
    public static char randomASCII() {
        return ' ';
    }

    public static char randomNumberChar() {
        return randomChar(NUMBER_CHAR_ARR);
    }

    public static char randomLetterChar() {
        return randomChar(LETTER_CHAR_ARR);
    }

    public static char randomUpperLetterChar() {
        return randomChar(Arrays.copyOfRange(LETTER_CHAR_ARR, 26, LETTER_CHAR_ARR.length)); // A~Z

    }

    public static char randomLowerLetterChar() {
        return randomChar(Arrays.copyOfRange(LETTER_CHAR_ARR, 0, 26)); // a~z
    }

    public static char randomChar(char[] arr) {

        int index = randomInt(0, arr.length); // 随机生成一个字符串下标
        return arr[index];
    }

    public static char randomChar() {
        return randomChar(CHAR_ARR);
    }

    // ---------------------------------------
    // 随机生成字符串类型
    // ---------------------------------------
    public static String randomString(char[] charArr, int minLen,int maxLen) {

        int len = randomInt(minLen,maxLen);
        char[] str = new char[len];
        for (int i = 0; i < len; i++) {
            str[i] = randomChar(charArr);
        }
        return new String(str);
    }

    public static String randomString() {
        return randomString(CHAR_ARR, STR_MIN_LEN,STR_MAX_LEN);
    }

    // ----------------------------------------
    // 随机生成普通的类对象
    // ----------------------------------------
    /**
     * @param type
     * @return
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     */
    public static <T> T randomObject(Class<T> type)
            throws InstantiationException, IllegalAccessException {
        
        T instance = null; //对象
        // 特殊类型特殊处理 //
        if(isCollection(type)) {
            throw new IllegalArgumentException("暂不支持Collection类型,请使用randomCollection()方法");
        } else if(isMap(type)) {
            throw new IllegalArgumentException("暂不支持Map类型,请使用randomMap()方法");
        } else if(isString(type)) {
            throw new IllegalArgumentException("暂不支持数组类型,请使用randomArray()方法");
        } else if(isArray(type)) {
            return (T) randomArray(type);
        } else if (isLong(type)) { //包装类型
            return (T) new Long(randomLong());
        } else if (isInt(type)) {
            return (T) new Integer(randomInt());
        } else if (isShort(type)) {
            return (T) new Short(randomShort());
        } else if (isByte(type)) {
            return (T) new Byte(randomByte());
        } else if (isDouble(type)) {
            return (T) new Double(randomDouble());
        } else if (isFloat(type)) {
            return (T) new Float(randomFloat());
        } else if (isChar(type)) {
            return (T) new Character(randomChar());
        } else if (isBoolean(type)) {
            return (T) new Boolean(randomBoolean());
        } else { //普通的类型
            instance = type.newInstance();
        }
        
        // 普通的类型 //
        Field[] fieldArr = type.getDeclaredFields();
        for (int i = 0; i < fieldArr.length; i++) {
            Field field = fieldArr[i];
            Class fieldType = field.getType();
            field.setAccessible(true);
            randomSetField(field,fieldType,instance);
        }
        return instance;
    }

    private static <T> void randomSetField(Field field,Class fieldType, T instance)
            throws IllegalArgumentException, IllegalAccessException, InstantiationException {

        
        if (isCollection(fieldType)) { //集合
            
        } else if (isMap(fieldType)) { //映射
            
        } else if (isArray(fieldType)) { //数组
            
        } else if ( isString(fieldType)) { //字符串
            field.set(instance, randomString());
        } else if(fieldType.isPrimitive()) { //基础类型
            randomSetPrimitiveField(field,fieldType,instance); //设置基础类型
        } else if (isWrapperLong(fieldType)) { //包装类型
            field.set(instance, randomLong());
        } else if (isWrapperInt(fieldType)) {
            field.set(instance, randomInt());
        } else if (isWrapperShort(fieldType)) {
            field.set(instance, randomShort());
        } else if (isWrapperByte(fieldType)) {
            field.set(instance, randomByte());
        } else if (isWrapperDouble(fieldType)) {
            field.set(instance, randomDouble());
        } else if (isWrapperFloat(fieldType)) {
            field.set(instance, randomFloat());
        } else if (isWrapperChar(fieldType)) {
            field.set(instance, randomChar());
        } else if (isWrapperBoolean(fieldType)) {
            field.set(instance, randomBoolean());
        } else { //普通的类型
            Object obj = randomObject(fieldType);
            field.set(instance, obj);
        }
    }
    
    private static <T> void randomSetPrimitiveField(Field field,Class primitiveType,T instance) 
            throws IllegalArgumentException, IllegalAccessException {
        
        if (isPrimitiveLong(primitiveType)) {
            field.setLong(instance, randomLong());
        } else if (isPrimitiveInt(primitiveType)) {
            field.setInt(instance, randomInt());
        } else if (isPrimitiveShort(primitiveType)) {
            field.setShort(instance, randomShort());
        } else if (isPrimitiveByte(primitiveType)) {
            field.setByte(instance, randomByte());
        } else if (isPrimitiveDouble(primitiveType)) {
            field.setDouble(instance, randomDouble());
        } else if (isPrimitiveFloat(primitiveType)) {
            field.setFloat(instance, randomFloat());
        } else if (isPrimitiveChar(primitiveType)) {
            field.setChar(instance, randomChar());
        } else if (isPrimitiveBoolean(primitiveType)) {
            field.setBoolean(instance, randomBoolean());
        }
    }
    
    // -----------------------------------------
    // 一些判断类型的方法
    // -----------------------------------------
    private static boolean isCollection(Class<?> type) {
        return Collection.class.isAssignableFrom(type);
    }

    private static boolean isMap(Class<?> type) {
        return Map.class.isAssignableFrom(type);
    }

    private static boolean isString(Class<?> type) {
        return String.class.isAssignableFrom(type);
    }

    private static boolean isArray(Class<?> fieldType) {
        return fieldType.getClass().isArray();
    }

    private static boolean isPrimitiveLong(Class<?> type) {
        return long.class == type;
    }
    
    private static boolean isWrapperLong(Class<?> type) {
        return Long.class == type;
    }
    
    private static boolean isLong(Class<?> type) {
        return isWrapperLong(type) || isPrimitiveLong(type);
    }
    

    private static boolean isPrimitiveInt(Class<?> type) {
        return int.class == type;
    }
    
    private static boolean isWrapperInt(Class<?> type) {
        return Integer.class == type;
    }
    
    private static boolean isInt(Class<?> type) {
        return isWrapperInt(type) || isPrimitiveInt(type);
    }

    private static boolean isPrimitiveShort(Class<?> type) {
        return short.class == type;
    }
    
    private static boolean isWrapperShort(Class<?> type) {
        return Short.class == type;
    }
    
    private static boolean isShort(Class<?> type) {
        return isWrapperShort(type) || isPrimitiveShort(type);
    }

    private static boolean isPrimitiveByte(Class<?> type) {
        return byte.class == type;
    }
    
    private static boolean isWrapperByte(Class<?> type) {
        return byte.class == type;
    }
    
    private static boolean isByte(Class<?> type) {
        return isWrapperByte(type) || isPrimitiveByte(type);
    }

    private static boolean isPrimitiveDouble(Class<?> type) {
        return double.class == type;
    }
    
    private static boolean isWrapperDouble(Class<?> type) {
        return Double.class == type;
    }
    
    private static boolean isDouble(Class<?> type) {
        return isWrapperDouble(type) || isPrimitiveDouble(type);
    }

    private static boolean isPrimitiveFloat(Class<?> type) {
        return float.class == type;
    }

    private static boolean isWrapperFloat(Class<?> type) {
        return Float.class == type;
    }
    
    private static boolean isFloat(Class<?> type) {
        return isWrapperFloat(type) || isPrimitiveFloat(type);
    }

    private static boolean isPrimitiveBoolean(Class<?> type) {
        return boolean.class == type;
    }
    
    private static boolean isWrapperBoolean(Class<?> type) {
        return Byte.class == type;
    }
    
    private static boolean isBoolean(Class<?> type) {
        return isWrapperBoolean(type) || isPrimitiveBoolean(type);
    }

    private static boolean isPrimitiveChar(Class<?> type) {
        return char.class == type;
    }
    
    private static boolean isWrapperChar(Class<?> type) {
        return Character.class == type;
    }
    
    private static boolean isChar(Class<?> type) {
        return isWrapperChar(type) || isPrimitiveChar(type);
    }

    // ----------------------------------------
    // 随机生成bean对象
    // ----------------------------------------
    /**
     * @param type
     * @return
     * 要求type表示的类必须有默认构造函数,与randomObject不同之处在于利用内省机制，仅对含有
     * setter方法的数据域进行随机数据填充
     */
    public static <T> T randomBean(Class<T> type) {
        return null;
    }

    // -----------------------------------------
    // 随机生成数组
    // -----------------------------------------
    
    /**
     * @param type
     * @param minLen
     * @param maxLen
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * 随机生成长度为minLen到maxLen，元素类型为type的数组
     */
    public static <T> T[] randomArray(Class<T> type,int minLen,int maxLen) 
            throws InstantiationException, IllegalAccessException {
        
       
        int len = randomInt(minLen,maxLen);
        T[] arr = (T[]) Array.newInstance(type,len);
        if(type.isArray()) {
            throw new IllegalArgumentException("暂不支持二维数组:"+type.getClass());
        }
        for(int i=0;i<len;i++) {
            arr[i] = randomObject(type);
        }
        return arr;
    }
    
    public static <T> T[] randomArray(Class<T> type) 
            throws InstantiationException, IllegalAccessException {
        return randomArray(type,ARR_MIN_LEN,ARR_MAX_LEN);
    }
    
    // -----------------------------------------
    // 随机生成Collection
    // -----------------------------------------
    public static <T> Collection<T> randomCollection(Collection<T> collection,Class<T> type) 
            throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException {
        
        return randomCollection(collection,type,COLL_MIN_SIZE,COLL_MAX_SIZE);
    }
    
    //TODO
    private static <T> Class<T> getElementType(Collection<T> collection) {
        return null;
    }
    
    /**
     * 
     * @param collection
     * @param type
     * @param minSize
     * @param maxSize
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static <T> Collection<T> randomCollection(Collection<T> collection,Class<T> type,int minSize,int maxSize) throws InstantiationException, IllegalAccessException {
        
        int len = randomInt(minSize,maxSize);
        for(int i=0;i<len;i++) {
            T element = randomObject(type);
            if(collection instanceof Set) {
                if(collection.contains(element))
                    continue;
            }
            collection.add(element);
        }
        return collection;
    }
    // -----------------------------------------
    // 随机生成Map
    // -----------------------------------------
    public static <K,V> Map<K,V> randomMap(Map<K,V> map,Class<K> keyType,Class<V> valueType) 
            throws InstantiationException, IllegalAccessException {
        return randomMap(map,keyType,valueType,MAP_MIN_SIZE,MAP_MAX_SIZE);
    }
    
    /**
     * @param map
     * @param keyType
     * @param valueType
     * @param minSize
     * @param maxSize
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * 随机返回一个size可能在minSize到maxSize之间的映射，之所以称之为可能是因为后面产生的
     * 随机对象替换了前面产生的随机对象
     */
    public static <K,V> Map<K,V> randomMap(Map<K,V> map,Class<K> keyType,Class<V> valueType,
            int minSize,int maxSize) throws InstantiationException, IllegalAccessException {
        
        int len = randomInt(minSize,maxSize);
        for(int i=0;i<len;i++) {
            K key = randomObject(keyType);
            if(map.containsKey(key)) {
                continue;
            }
            map.put(key,randomObject(valueType));
        }
        return map;
    }
    
    //TODO
    private static <K,V> Class<K> getKeyType(Map<K,V> map) {
        return null;
    }
    
    //TODO
    private static <K,V> Class<V> getValueType(Map<K,V> map) {
        return null;
    }
}
