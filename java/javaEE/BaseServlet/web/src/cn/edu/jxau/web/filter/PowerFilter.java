package cn.edu.jxau.web.filter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.edu.jxau.bean.User;
import cn.edu.jxau.utils.LogUtil;

public class PowerFilter implements Filter {
	
	private FilterConfig fConfig;
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse rps = (HttpServletResponse)response;
		HttpSession session = req.getSession();
		User user = (User)session.getAttribute("user");
		if(user!=null) { //如果用户登录了
			int len = fConfig.getServletContext().getContextPath().length();
			String URI = req.getRequestURI();
			URI = URI.substring(len);
			Map<Integer,List<String>> powerMap = (Map<Integer,List<String>>)fConfig.getServletContext().getAttribute("powerMap");
			if(!this.isAllow(powerMap,user.getRoleID(),URI)) {//没有权限，则转发至404.jsp
				LogUtil.info(35,"拦截用户："+user.getUsername());
				LogUtil.info(36,"用户去向："+req.getRequestURI());
				req.getRequestDispatcher("/WEN-INF/404.jsp").forward(req,rps);
				return;
			}
		} else { //如果用户没有登录
			
			 
		}
		chain.doFilter(req, rps);
	}
	private boolean isAllow(Map<Integer,List<String>> powerMap,int roleID,String URI){
		
		
		List<String> pageList = powerMap.get(roleID);
		for(int i=0;i<pageList.size();i++) {
			if(URI.startsWith(pageList.get(i))) {
				
				return true;
			}
		}
		return false;
	}
	@Override
	public void init(FilterConfig fConfig) throws ServletException {
		
		this.fConfig = fConfig;
	}
    @Override
	public void destroy() {
	
    }
}
