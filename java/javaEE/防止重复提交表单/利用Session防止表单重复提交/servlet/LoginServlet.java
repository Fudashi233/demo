package cn.edu.jxau.web.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.edu.jxau.web.filter.TokenFilter;

public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();//阻塞3s,模拟网络延迟
        }
        if(isRepeat(request)) {
            System.out.println("请勿重复提交表单");
        } else {
            System.out.println("向数据库中插入数据");
            request.getSession().removeAttribute(TokenFilter.TOKEN);
        }
    }
    
    private boolean isRepeat(HttpServletRequest request) {
        
        String token = (String) request.getSession().getAttribute(TokenFilter.TOKEN);
        if(token == null) { // session中不包含token数据
            return true;
        }
        if(token.equals(request.getParameter("token"))) {
            return false;
        }
        return true;
    }
}
