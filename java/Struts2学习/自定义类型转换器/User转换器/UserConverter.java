package cn.edu.jxau.converter;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;

import cn.edu.jxau.entity.User;

public class UserConverter extends StrutsTypeConverter {

    @Override
    public Object convertFromString(Map context, String[] values, Class toClass) {

        System.out.println(Arrays.toString(values));
        if (values.length == 1) {

            String userStr = values[0];
            User user = new User();
            user.setUsername(userStr.substring(0, userStr.indexOf(",")));
            user.setPassword(userStr.substring(userStr.indexOf(",") + 1));
            return user;
        } else {

            User[] users = new User[values.length];
            for (int i = 0; i < values.length; i++) {

                User user = new User();
                String userStr = values[i];
                user.setUsername(userStr.substring(0, userStr.indexOf(",")));
                user.setPassword(userStr.substring(userStr.indexOf(",") + 1));
                users[i] = user;
            }
            return users;
        }
    }

    @Override
    public String convertToString(Map context, Object o) {

        if (o instanceof User) {
            User user = (User)o;
            return String.format("%s,%s",user.getUsername(),user.getPassword());
        } else if(o instanceof User[]) {
            
            StringBuilder builder = new StringBuilder();
            User[] userArr = (User[])o;
            for(User user : userArr) {
                builder.append(String.format("%s,%s",user.getUsername(),user.getPassword()));
            }
            return builder.toString();
        } else {
            throw new IllegalArgumentException("参数o必须是User对象或者是User数组对象");
        }
    }
}
