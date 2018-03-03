package cn.edu.jxau.web.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.edu.jxau.bean.ServiceResult;
import cn.edu.jxau.bean.User;
import cn.edu.jxau.utils.LogUtil;


public class UserServlet extends BaseServlet {

	private String login(HttpServletRequest request, HttpServletResponse response) {
	
		/*获取参数，构造user对象*/
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String roleID = request.getParameter("roleID");
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user.setRoleID(Integer.parseInt(roleID));
		/*放进session中*/
		request.getSession().setAttribute("user",user);
		LogUtil.info(29,user);
		/*转发至select.jsp*/
		return ServiceResult.SUCCESS;
	}
	private String logout(HttpServletRequest request,HttpServletResponse response) {
		
		request.getSession().invalidate();
		return ServiceResult.SUCCESS;
	}
}
