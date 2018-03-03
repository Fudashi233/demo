package cn.edu.jxau.action;

import java.util.ArrayList;
import java.util.List;

public class UserAction {
    
    private String username;
    private String password;
    private List<String> list;
    
    public UserAction() {
        
    }
    
    public String login() {
        
        System.out.printf("username=%s,password=%s",username,password);
        if("fudashi233".equals(username) && "123465".equals(password)) {
            System.out.println("登录成功");
            list = new ArrayList<>();
            list.add("a");
            list.add("b");
            list.add("c");
            return "success";
        } else {
            System.out.println("登录失败");
            return "error";
        }
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

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}
