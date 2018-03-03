package cn.edu.jxau.interceptor;

import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.StrutsStatics;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class LogInterceptor extends AbstractInterceptor {
    
    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        
        HttpServletRequest beforeRequest = (HttpServletRequest) invocation.getInvocationContext().get(StrutsStatics.HTTP_REQUEST);
        String beforeParam = getParam(beforeRequest);
        long start = System.currentTimeMillis();
        String result = invocation.invoke();
        long end = System.currentTimeMillis();
        HttpServletRequest afterRequest = (HttpServletRequest) invocation.getInvocationContext().get(StrutsStatics.HTTP_REQUEST);
        String afterParam = getParam(afterRequest);
        System.out.println(getLog(beforeRequest.getRequestURI(),start, end, beforeParam, afterParam));
        return result;
    }

    private String getLog(String URI, long start, long end, String beforeParam, String afterParam) {

        StringBuilder logBuilder = new StringBuilder();
        logBuilder.append("↓--------------------------\n");
        logBuilder.append(String.format("URI:%s\n\n",URI));
        logBuilder.append(String.format("start:%s\n\n", new Date(start))); // 发起请求的时间
        logBuilder.append(String.format("time:%d ms\n\n", end - start)); // 处理请求耗时
        logBuilder.append(String.format("parameter of request before action:\n%s\n\n",beforeParam));
        logBuilder.append(String.format("parameter of request after action:\n%s\n",afterParam));
        logBuilder.append("↑--------------------------\n\n\n");
        return logBuilder.toString();
    }
    
    private String getParam(ServletRequest request) {
        
        StringBuilder paramBuilder = new StringBuilder();
        Map<String, String[]> paramMap = request.getParameterMap();
        Iterator<Entry<String,String[]>> iterator = paramMap.entrySet().iterator();
        int size = paramMap.size();
        int index = 0;
        while(iterator.hasNext()) {
            index++;
            Entry<String,String[]> entry = iterator.next();
            paramBuilder.append(entry.getKey()).append(":");
            paramBuilder.append(Arrays.toString(entry.getValue()));
            if(index != size) {
                paramBuilder.append("\n");
            }
        }
        if(paramBuilder.length() == 0) {
            paramBuilder.append("null");
        }
        return paramBuilder.toString();
    }
}
