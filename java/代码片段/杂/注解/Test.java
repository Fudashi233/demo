//import java.lang.reflect.Field;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//
//import java.lang.annotation.Annotation;
//
//public class Test
//{
//    public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, NoSuchMethodException, SecurityException, InvocationTargetException
//    {
//    	StudentFilter f = new StudentFilter();
//    	f.setName("QWE");
//    	System.out.println(select(f));
//    }
//    @SuppressWarnings("unchecked")
//    public static String select(StudentFilter filter) throws IllegalArgumentException, IllegalAccessException, NoSuchMethodException, SecurityException, InvocationTargetException
//    {
//    	StringBuilder builder = new StringBuilder("SELECT * FROM ");
//    	
//		Class<StudentFilter> c = (Class<StudentFilter>) filter.getClass();
//    	if(!c.isAnnotationPresent(Table.class))
//    	{
//    		return null;
//    	}
//    	//  获取表名
//    	Table a = c.getAnnotation(Table.class);
//    	String tableName = c.getAnnotation(Table.class).value();
//    	
//    	builder.append(tableName);
//    	builder.append(" WHERE 1=1 ");
//    	
//    	Field[] fieArr = c.getDeclaredFields();
//    	for(int i=0;i<fieArr.length;i++)
//    	{
//    		if(!fieArr[i].isAnnotationPresent(Column.class))
//    		{
//    			continue;   //无注解或者无数据，跳出循环
//    		}
//    	    String columnName = fieArr[i].getAnnotation(Column.class).value();
//    	    String fieldName = fieArr[i].getName();
//    	    String methodName = "get"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
//    	    Method method = c.getMethod(methodName);
//    	    Object value = method.invoke(filter,null);
//    	    //判断数据值，缩短sql语句
//    	    if(value==null)
//    	    	continue;
//    	    if(value instanceof Integer&&(Integer)value == 0)
//    	    	continue;
//    	    builder.append(" AND ");
//    	    builder.append(columnName);
//    	    builder.append(" = ");
//    	    //字符串类型加单引号
//    	    if(value instanceof String )
//    	    {
//    	    	builder.append("'");
//    	    	builder.append(value);
//    	        builder.append("'");
//    	    }    	  
//    	    else
//    	    {
//    	    	builder.append(value);
//    	    }
//    	    builder.append(" ");
//    	}
//    	
//    	return builder.toString();
//    }
//}
@Table(name = "QWE",age = "17")
public class Test
{
	public static void main(String[] args)
	{
		Class<?> c = Test.class;
		Table a = c.getAnnotation(Table.class);
		System.out.println(a.name());
	}
}
