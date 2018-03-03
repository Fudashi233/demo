package cn.edu.jxau.core.filter;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;

import cn.edu.jxau.core.Action;
import cn.edu.jxau.core.ActionMapper;
import cn.edu.jxau.core.Result;

public class DispatcherFilter implements Filter {

    private ActionMapper actionMapper;

    @Override
    public void init(FilterConfig config) throws ServletException {

        InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("framework.properties");
        Properties prop = new Properties();
        try {
            prop.load(input);
        } catch (IOException e) {
            throw new ServletException("framework.properties配置文件加载失败",e);
        }
        String actionsXMLPath = prop.getProperty("actionsXMLPath");
        try {
            actionMapper = new ActionMapper(actionsXMLPath);
        } catch (DocumentException e) {
            throw new RuntimeException("actions.xml配置文件加载失败", e);
        }
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) {

        Action action = actionMapper.getAction((HttpServletRequest) req);
        System.out.printf("action:%s\n", action);
        if (action == null) {
            throw new RuntimeException("没有对应的Action处理该请求,请添加相应配置");
        } else {
            try {
                Class<?> actionClass = Class.forName(action.getClazz());
                Object actionInstance = newActionInstance((HttpServletRequest) req, actionClass);
                String executeResult = executeAction(actionInstance, action.getMethod());// 执行action
                Result result = action.getResultMap().get(executeResult);
                if(result==null) {
                    throw new RuntimeException("没有对应的Result处理该请求,请添加相应配置");
                }
                System.out.printf("result:%s\n", result);
                dispatcher((HttpServletRequest) req, (HttpServletResponse) res, actionInstance, result);
            } catch (Exception e) {
                throw new RuntimeException("方法调用失败", e);
            }
        }
    }

    private void dispatcher(HttpServletRequest request, HttpServletResponse response, Object actionInstance,
            Result result) throws IOException, ServletException, IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        String type = result.getType();
        if ("dispatcher".equals(type)) { // 转发
            setRequestAttribute(request, actionInstance); //将action中的属性设置进request中
            request.getRequestDispatcher(result.getLocation()).forward(request, response);
        } else if ("redirect".equals(type)) { // 重定向
            response.sendRedirect(request.getContextPath()+result.getLocation());
        } else {
            throw new RuntimeException(String.format("<result>配置错误，没有处理%s的Result类型", type));
        }
    }

    private void setRequestAttribute(HttpServletRequest request, Object actionInstance) throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        Field[] fieldArr = actionInstance.getClass().getDeclaredFields();
        for(Field field : fieldArr) {
            field.setAccessible(true);
            request.setAttribute(field.getName(),field.get(actionInstance));
        }
    }

    private String executeAction(Object actionInstance, String methodName) throws IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

        Method method = actionInstance.getClass().getDeclaredMethod(methodName);
        String result = (String) method.invoke(actionInstance);
        return result;
    }

    private Object newActionInstance(HttpServletRequest request, Class<?> actionClass) throws InstantiationException,
            IllegalAccessException, IntrospectionException, IllegalArgumentException, InvocationTargetException {

        Object actionInstance = actionClass.newInstance();
        Map<String, String[]> parameterMap = request.getParameterMap();
        Iterator<Entry<String, String[]>> iterator = parameterMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<String, String[]> entry = iterator.next();
            String key = entry.getKey();
            String[] value = entry.getValue();
            PropertyDescriptor propertyDesc = new PropertyDescriptor(key, actionClass);
            Method writeMethod = propertyDesc.getWriteMethod();
            if (writeMethod.getParameters()[0].getClass().isArray()) {
                writeMethod.invoke(actionInstance, value);
            } else {
                writeMethod.invoke(actionInstance, value[0]);
            }
        }
        return actionInstance;
    }

    @Override
    public void destroy() {

    }
}
