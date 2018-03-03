package cn.edu.jxau.service;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;

import cn.edu.jxau.controler.core.Model;
import cn.edu.jxau.controler.core.Result;
import cn.edu.jxau.entity.User;

public class UserService {
    
    private UserService() {
        throw new UnsupportedOperationException("不可实例化UserService");
    }
    
    public static String login(Model model) {
        
        User user;
        try {
            user = model.getBean(User.class);
            System.out.println(user);
            model.put("session_user",user);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.NAME_FAILURE;
        }
        return Result.NAME_UPDATE_SESSION;
    }
    
    public static String logout(Model model) {
        return Result.NAME_INVALIDATE_SESSION;
    }
}

