package cn.edu.jxau.entity;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

public class User {
    
    private String username;
    private String password;
    private int age;
    
    public static void main(String[] args) throws IntrospectionException {
        
        PropertyDescriptor[] propertyDescArr = Introspector.getBeanInfo(User.class).getPropertyDescriptors();
        for(PropertyDescriptor propertyDesc : propertyDescArr) {
            System.out.println(propertyDesc.getName());
        }
    } 
    
    public User() {
        
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public int getAge() {
        return age;
    }
    
    public void setAge(int age) {
        this.age = age;
    }
    
    @Override
    public String toString() {
        return "User [username=" + username + ", password=" + password + ", age=" + age + "]";
    }
}
