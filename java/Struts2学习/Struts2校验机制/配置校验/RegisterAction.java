package cn.edu.jxau.test;

import java.util.Date;

import com.opensymphony.xwork2.ActionSupport;

public class RegisterAction extends ActionSupport {
    
    /**
     * {@field username}不能为空，内容只能是字母和数字，字符串长度范围[4,25]
     */
    private String username;
    
    /**
     * {@field password}不能为空，内容只能是字母和数字，字符串长度范围[4,25]
     */
    private String password;
    
    /**
     * {@field age}的范围是[1,150]
     */
    private int age;
    
    /**
     * {@field birtyday}必须在1900-01-01到2050-02-21之间
     */
    private Date birthday;
    
    public RegisterAction() {
        
    }

    public String register() {
        
        System.out.printf("username:%s\n",username);
        System.out.printf("password:%s\n",password);
        System.out.printf("age:%d\n",age);
        System.out.printf("birthday:%s\n",birthday);
        return "success";
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

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
}
