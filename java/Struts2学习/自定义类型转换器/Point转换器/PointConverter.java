package cn.edu.jxau.converter;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;

import cn.edu.jxau.test.Point;


public class PointConverter extends StrutsTypeConverter {

    @Override
    public Object convertFromString(Map context, String[] values, Class toClass) {
        
        Iterator iterator = context.entrySet().iterator();
        while(iterator.hasNext()) {
            Object key = iterator.next();
            Object value = context.get(key);
            System.out.println(key+"    "+value);
        }
        System.out.println("--------------------------------------");
        String pointStr = values[0];
        Point point = new Point();
        point.setX(Integer.parseInt(pointStr.substring(1,pointStr.indexOf(","))));
        point.setY(Integer.parseInt(pointStr.substring(pointStr.indexOf(",")+1,pointStr.length()-1)));
        return point;
    }

    @Override
    public String convertToString(Map context, Object o) {
        
        Iterator iterator = context.entrySet().iterator();
        while(iterator.hasNext()) {
            Object key = iterator.next();
            Object value = context.get(key);
            System.out.println(key+"    "+value);
        }
        Point point = (Point)o;
        return String.format("(%d,%d)",point.getX(),point.getY());
    }

}
