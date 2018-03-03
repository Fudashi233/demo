package cn.edu.jxau.web.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.edu.jxau.bean.Action;
import cn.edu.jxau.bean.Result;
import cn.edu.jxau.bean.ServiceResult;
import cn.edu.jxau.utils.LogUtil;

public abstract class BaseServlet extends HttpServlet {
	
	@Override
	protected void service(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
		
		/*获取调用的方法名*/
		String methodName = request.getParameter("methodName");
		if(methodName == null || methodName.trim().isEmpty()) {
			
			throw new RuntimeException("methodName请求参数缺失");
		}
		Class<?> clazz = this.getClass();
		LogUtil.info(32,"业务类："+clazz);
		LogUtil.info(33,"业务方法："+methodName);
		Action action = getAction(clazz.getName(),methodName);
		if(action == null)
			throw new RuntimeException("配置文件异常，请检查"+clazz+"相应的action的配置项");
		/*调用相应的方法*/
		String resultInfo = null;//业务方法的调用情况，用于获取跳转方式和跳转目的地
		try {
			Method method = clazz.getDeclaredMethod(methodName,HttpServletRequest.class,HttpServletResponse.class);
			method.setAccessible(true);
			resultInfo = (String)method.invoke(this,request,response);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		/*跳转到相应页面*/
		if(resultInfo!=null && !resultInfo.trim().isEmpty()) {
			
			Result result = action.getResultMap().get(resultInfo);
			String destinationURI = result.getDestinationURI();
			String type = result.getType();
			LogUtil.info(60,"执行情况："+resultInfo);
			LogUtil.info(61,"跳转形式："+type);
			LogUtil.info(62,"目的页面："+destinationURI);
			this.skip(request,response,type,destinationURI);
		} else {
			
			throw new RuntimeException("配置文件异常，请检查"+clazz+"相应的action的配置项");
		}
	}
	private Action getAction(String className,String methodName) {
		
		/*参数验证*/
		if(className == null || className.trim().isEmpty()) {
			
			throw new IllegalArgumentException("className不能为空");
		}
		if(methodName == null || methodName.trim().isEmpty()) {
		
			throw new IllegalArgumentException("requestURI不能为空");
		}
		/*裁剪requestURI,去除contextPath，以便于与sourceURI对比*/
		int len = super.getServletContext().getContextPath().length();
		Map<String,List<Action>> serviceMap = (Map<String,List<Action>>)super.getServletContext().getAttribute("serviceMap");
		List<Action> actionList = serviceMap.get(className);
		if(actionList==null)
			throw new RuntimeException("配置文件异常，请检查"+className+"的相应action配置项");
		Iterator<Action> actionIterator = actionList.iterator();
		/*遍历数组，找出与sourceURI对应的action*/
		while(actionIterator.hasNext()) {
			
			Action action = actionIterator.next();
			String actionMethodName = action.getMethodName();
			if(actionMethodName.equals(methodName)) {
				
				return action;
			}
		}
		return null;//配置文件出错，相应的sourceURI未配置
	}
	private void skip(HttpServletRequest request,HttpServletResponse response,String type,String destination) throws ServletException, IOException{
		
		if(type.equals(ServiceResult.FORWARD)) {	//转发
			
			request.getRequestDispatcher(destination).forward(request,response);
			return ;
		} else if(type.equals(ServiceResult.REDIRECT)) { //重定向
			
			response.sendRedirect(request.getContextPath()+destination);
		}
	}
}