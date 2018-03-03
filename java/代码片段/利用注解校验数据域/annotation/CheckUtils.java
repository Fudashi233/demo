package annotation;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;

public class CheckUtils {
    
    /**
     * 校验obj中所有含有注解的数据域
     * @param obj
     * @return
     * @throws CheckException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static boolean checkObject(Object obj) 
            throws CheckException, IllegalArgumentException, IllegalAccessException{
        
        Field[] fieldArr = obj.getClass().getDeclaredFields();
        for(int i=0;i<fieldArr.length;i++) {
            Field field = fieldArr[i];
            field.setAccessible(true);
            if(field.isAnnotationPresent(StringCheck.class)) {
                checkString((String)field.get(obj),field.getAnnotation(StringCheck.class)); //校验字符串
            } else if(field.isAnnotationPresent(NumberCheck.class)) {
                checkInteger((Number)field.get(obj),field.getAnnotation(NumberCheck.class)); //校验整数
            } else if(field.isAnnotationPresent(CollectionCheck.class)) {
                checkCollection(field.get(obj),field.getAnnotation(CollectionCheck.class)); //校验集合
            }
        }
        return true;
    }
    
    /**
     * 校验obj中所有含有注解的且含有对应的getter/setter方法的数据域
     * 利用了内省机制获取符合上述条件的数据域
     * @param obj
     * @return
     * @throws CheckException 
     * @throws IllegalAccessException 
     * @throws IllegalArgumentException 
     */
    public static boolean checkBean(Object obj) throws IllegalArgumentException, IllegalAccessException, CheckException {
        Field[] fieldArr = obj.getClass().getDeclaredFields();
        for(int i=0;i<fieldArr.length;i++) {
            Field field = fieldArr[i];
            field.setAccessible(true);
            try {
                Object value = getBeanFieldValue(field,obj);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IntrospectionException e) { //抛出这个异常，说明该field没有对应的getter、setter方法，也就不符合javabean规定，不参与校验
                continue;
            }
            if(field.isAnnotationPresent(StringCheck.class)) {
                checkString((String)field.get(obj),field.getAnnotation(StringCheck.class)); //校验字符串
            } else if(field.isAnnotationPresent(NumberCheck.class)) {
                checkInteger((Number)field.get(obj),field.getAnnotation(NumberCheck.class)); //校验整数
            } else if(field.isAnnotationPresent(CollectionCheck.class)) {
                checkCollection(field.get(obj),field.getAnnotation(CollectionCheck.class)); //校验集合
            }
        }
        return true;
    }
    
    private static Object getBeanFieldValue(Field field,Object obj) throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        System.out.println(field.getName());
        PropertyDescriptor PD = new PropertyDescriptor(field.getName(),obj.getClass());
        Method method = PD.getReadMethod();
        return method.invoke(obj);
    }
    
    /**
     * 根据annotation校验value
     * @param value
     * @param annotation
     * @return
     * @throws CheckException 
     */
    private static boolean checkString(String value,StringCheck annotation) throws CheckException {
        
        // 是否为空 //
        boolean notNull = annotation.notNull();
        if(notNull) {
            if(value==null) {
                throw new CheckException("value不能为null");
            }
        }
        if(value==null) { //value为空，下面的校验也就不需要执行了
            return true;
        }
        
        // 验证长度 //
        int minLen = annotation.minLen();
        int maxLen = annotation.maxLen();
        int strLen = value.length();
        if(minLen>maxLen) {
            throw new RuntimeException("StringCheck注解配置错误，minLen不能大于maxLen");
        }
        if(strLen<minLen || strLen>maxLen) {
            throw new CheckException(String.format("value长度请保证在%d-%d之间",minLen,maxLen));
        }
        
        // 正则表达式 //
        String regExp = annotation.regExp();
        if(!regExp.trim().equals("")) {
            Pattern pattern = Pattern.compile(regExp,annotation.flags());
            if(!pattern.matcher(value).matches()) {
                throw new CheckException(String.format("value无法匹配正则表达式:%s",regExp));
            }
        }
        return true;
    }
    
    private static boolean checkInteger(Number value,NumberCheck annotation) throws CheckException {
        
        // 是否为空 //
        boolean notNull = annotation.notNull();
        if(notNull) {
            if(value==null) {
                throw new CheckException("value不能为null");
            }
        }
        if(value==null) { //value为空，下面的校验也就不需要执行了
            return true;
        }
        
        // 验证大小 //
        long min = annotation.min();
        long max = annotation.max();
        if(min>max) {
            throw new RuntimeException("NumberCheck注解配置错误，min不能大于max");
        }
        long val = value.longValue();
        if(val<min || val>max) {
            throw new CheckException(String.format("value大小请保证在%d-%d之间",min,max));
        }
        
        // 特殊性验证,具体见{@Code IntegerCheck} //
        long[] in = annotation.in();
        if(in.length!=0) {
            if(annotation.not()) { //not in
                for(int i=0;i<in.length;i++) {
                    if(in[i]==val) {
                        throw new CheckException(String.format("value值不能在%s中",Arrays.toString(in)));
                    }
                }
            } else { //in
                boolean isIn = false; //如果在in数值中，则为true，不在则为false
                for(int i=0;i<in.length;i++) {
                    if(in[i]==val) {
                        isIn = true;
                        break;
                    }
                }
                if(!isIn) {
                    throw new CheckException(String.format("value值必须在%s中",Arrays.toString(in)));
                }
            }
        }
        return true;
    }
    
    private static boolean checkCollection(Object value,CollectionCheck annotation) throws CheckException {
        
        // 是否为空 //
        boolean notNull = annotation.notNull();
        if(notNull) {
            if(value==null) {
                throw new CheckException("value不能为null");
            }
        }
        if(value==null) { //value为空，下面的校验也就不需要执行了
            return true;
        }
        
        // 校验集合大小 //
        int minSize = annotation.minSize();
        int maxSize = annotation.maxSize();
        int size = getCollectionSize(value);
        if(minSize>maxSize) {
            throw new RuntimeException("CollectionCheck注解配置错误，minSize不能大于maxSize");
        }
        if(size<minSize || size>maxSize) {
            throw new CheckException(String.format("集合大小请保证在%d-%d之间",minSize,maxSize));
        }
        return true;
    }
    
    private static int getCollectionSize(Object obj) {
        
        if(obj.getClass().isArray()) {
            return Array.getLength(obj);
        }
        if(obj instanceof Collection) {
            return ((Collection) obj).size();
        }
        if(obj instanceof Map) {
            return ((Map) obj).size();
        }
        throw new RuntimeException("CollectionCheck注解配置错误,其仅能用于数组，集合，映射中");
    }
}
