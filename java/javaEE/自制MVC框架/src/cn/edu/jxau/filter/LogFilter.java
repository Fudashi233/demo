package cn.edu.jxau.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * 日志过滤器，用于记录请求日志
 * @author 付大石
 */
public class LogFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        System.out.println("↓------------------------------");
        System.out.println("请求时间："+new Date());
        System.out.println("请求URI："+httpRequest.getRequestURI());
        System.out.println("请求方式："+httpRequest.getMethod());
        System.out.println("请求参数："+parameterMapToString(httpRequest.getParameterMap()));
        System.out.println("↑------------------------------\n\n");
        chain.doFilter(request,response);
    }
    
    public String parameterMapToString(Map<String, String[]> paramMap) {
        
        StringBuilder builder = new StringBuilder();
        Iterator<Map.Entry<String,String[]>> iterator = paramMap.entrySet().iterator();
        while(iterator.hasNext()) {
            
            Entry<String,String[]> entry = iterator.next();
            String key = entry.getKey();
            String[] value = entry.getValue();
            builder.append(key).append("-").append(Arrays.toString(value)).append("\n");
        }
        if(builder.length()>=1) { //删除最后对于的'\n'
            builder.deleteCharAt(builder.length()-1);
        }
        return builder.toString();
    }

    @Override
    public void destroy() {
        
    }

}
