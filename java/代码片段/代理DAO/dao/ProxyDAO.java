package cn.edu.jxau.dao;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

public class ProxyDAO implements InvocationHandler{
    
    
    //======================================
    // SQL模板
    //======================================
    private static final String INSERT = "INSERT INTO %s(%s) VALUES(%s)"; //表名、属性名、属性值
    private static final String DELETE = "DELETE FROM %s WHERE %s=?"; //表名，条件
    private static final String UPDATE = "UPDATE %s SET %s WHERE %s=?"; //表名，属性，条件
    private static final String SELECT = "SELECT * FROM %s"; //表名
    private static final String SELECT_ONE = "SELECT * FROM %s WHEHE %s=?"; //表名，查询条件
    
    private String tableName;
    private String primaryKey;
    private Class<?> entityClass;
    private Class<?> idClass;
    private Map<String,String> fieldColumnMap; //实体类[数据域名称]到数据库[属性名称]的映射
    
    public ProxyDAO(Class<? extends CURD<?,?>> DAOClass) {
        
        fieldColumnMap = new HashMap<>();
        checkDAO(DAOClass);
    }
    
    /**
     * 校验实体类的注解是否完整并且符合规范
     * 
     * @return
     */
    private void checkDAO(Class<? extends CURD<?,?>> DAOClass) {
        
        Type type = DAOClass.getGenericInterfaces()[0];
        ParameterizedType paramType = (ParameterizedType) type;
        this.entityClass = (Class<?>) paramType.getActualTypeArguments()[0]; //实体类字节码
        this.idClass = (Class<?>) paramType.getActualTypeArguments()[1]; //主键类型字节码
        Annotation[] annotationArr = entityClass.getAnnotations();
        
        // 检查@Entity注解和@Table注解 //
        boolean hasEntity = false;
        boolean hasTable = false;
        for(Annotation annotation : annotationArr) {
            Class<? extends Annotation> annotationType = annotation.annotationType();
            if(annotationType == Entity.class) {
                hasEntity = true;
            } else if(annotationType == Table.class) {
                hasTable = true;
                this.tableName = ((Table)annotation).name();
                if("".equals(this.tableName.trim())) {
                    throw new RuntimeException("配置错误，表名未指定");
                }
            }
        }
        if(!hasEntity) {
            throw new RuntimeException("配置错误，实体类未配置@Entity注解");
        }
        if(!hasTable) {
            throw new RuntimeException("配置错误，实体类未配置@Table注解");
        }
        
        // 检查@Id注解,建立数据域名称与属性名称的映射 //
        boolean hasId = false;
        Field[] fieldArr = entityClass.getDeclaredFields();
        for(Field field : fieldArr) {
            if(field.isAnnotationPresent(Id.class)){
                hasId = true;
                this.primaryKey = field.getName();
            }
            if(field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);
                fieldColumnMap.put(field.getName(),column.name());
            } else {
                fieldColumnMap.put(field.getName(),field.getName());
            }
        }
        if(!hasId) {
            throw new RuntimeException("配置错误，实体类未配置@Id注解");
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        
        System.out.println(method.getName());
        String SQL = "";
        String methodName = method.getName();
        if(methodName.equals("insert")) {
            
            StringBuilder columnsBuilder = new StringBuilder();
            StringBuilder valuesBuilder = new StringBuilder();
            Object item = args[0];
            for(Field field : entityClass.getDeclaredFields()) {
                field.setAccessible(true);
                Object obj = field.get(item);
                if(obj!=null) {
                    columnsBuilder.append(fieldColumnMap.get(field.getName())).append(",");
                    if(obj.getClass()==String.class) { //字符串加单引号
                        valuesBuilder.append("'").append(obj).append("'").append(",");
                    } else {
                        valuesBuilder.append(obj).append(",");
                    }
                    
                }
            }
            columnsBuilder.deleteCharAt(columnsBuilder.length()-1);
            valuesBuilder.deleteCharAt(valuesBuilder.length()-1);
            SQL = String.format(INSERT,tableName,columnsBuilder.toString(),valuesBuilder.toString());
        } else if(methodName.equals("delete")) {
            SQL = String.format(DELETE,tableName,primaryKey);
        } else if(methodName.equals("update")) {
            StringBuilder setsBuilder = new StringBuilder();
            Object item = args[0];
            for(Field field : entityClass.getDeclaredFields()) {
                field.setAccessible(true);
                Object obj = field.get(item);
                if(obj!=null) {
                    if(obj.getClass()==String.class) { //字符串加单引号
                        setsBuilder.append(fieldColumnMap.get(field.getName()))
                        .append("=").append("'").append(obj)
                        .append("'").append(",");
                    } else {
                        setsBuilder.append(fieldColumnMap.get(field.getName())).append("=")
                            .append(obj).append(",");
                    }
                }
            }
            setsBuilder.deleteCharAt(setsBuilder.length()-1);
            SQL = String.format(UPDATE,tableName,setsBuilder.toString(),primaryKey);
        } else if(methodName.equals("select")) {
            SQL = String.format(SELECT,tableName);
        } else if(methodName.equals("selectOne")) {
            SQL = String.format(SELECT_ONE,tableName,primaryKey);
        }
        return null;
    }
}
