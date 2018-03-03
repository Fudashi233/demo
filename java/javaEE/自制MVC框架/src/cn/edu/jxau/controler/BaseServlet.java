package cn.edu.jxau.controler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUploadException;

import cn.edu.jxau.controler.core.Model;
import cn.edu.jxau.controler.core.Result;
import cn.edu.jxau.controler.core.Service;
import cn.edu.jxau.controler.core.ServiceMapper;
import cn.edu.jxau.util.IOUtils;

@WebServlet(value = "*.do", loadOnStartup = 0)
public class BaseServlet extends HttpServlet {

    @Override
    public void init() {
        
    }

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Service service = ServiceMapper.getService(request);
        if (service == null) { // service不存在，路径错误
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            throw new RuntimeException("配置文件配置错误，没有找到该请求对应的service对象");
        }
        try {
            // 调用业务方法 //
            Class<?> serviceClass = Class.forName(service.getClazz());
            Method method = serviceClass.getDeclaredMethod(service.getMethod(), Model.class);
            method.setAccessible(true);
            Model model = new Model(request);
            String resultName = (String) method.invoke(null, model);
            Result result = service.getResultMap().get(resultName);
            if (result == null) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                throw new RuntimeException(String.format("配置文件配置错误，没有找到%s对应的result对象", resultName));
            }

            // 处理结果 //
            dealResult(request, response ,model, result);

            // 将逻辑视图映射到物理视图 //
            if(!resultName.equals(Result.NAME_DOWNLOAD)) {
                dispatcher(request, response, model, result);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
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
        } catch (FileUploadException e) {
            e.printStackTrace();
        }
    }

    private void dispatcher(HttpServletRequest request, HttpServletResponse response, Model model, Result result)
            throws ServletException, IOException {

        String type = result.getType();
        if (Result.TYPE_DISPATCHER.equals(type)) { // 转发
            // 存储数据到request作用域中 //
            Map<String, Object> data = model.getParamMap();
            if (!data.isEmpty()) {
                Iterator<Entry<String, Object>> iterator = data.entrySet().iterator();
                while (iterator.hasNext()) {
                    Entry<String, Object> entry = iterator.next();
                    request.setAttribute(entry.getKey(), entry.getValue());
                }
            }
            request.getRequestDispatcher(result.getLocation()).forward(request, response);
        } else if (Result.TYPE_REDIRECT.equals(type)) { // 重定向
            response.sendRedirect(request.getContextPath() + result.getLocation());
        } else {
            throw new RuntimeException(String.format("<result>配置错误，没有处理%s的Result类型", type));
        }
    }

    /**
     * 根据业务方法的执行结果进行特殊处理
     * @param request
     * @param model
     * @param result
     * @throws IOException 
     */
    private void dealResult(HttpServletRequest request, HttpServletResponse response, Model model, Result result) throws IOException {

        // 处理特殊的result name //
        String resultName = result.getName();
        if (resultName.equals(Result.NAME_UPDATE_SESSION)) { //更新session
            Iterator<Entry<String,Object>> iterator = model.getParamMap().entrySet().iterator();
            while(iterator.hasNext()) {
                Entry<String,Object> entry = iterator.next();
                String key = entry.getKey();
                Object value = entry.getValue();
                if(key.startsWith("session_")) { //需要session中的键值对
                    key = key.substring("session_".length());
                    request.getSession().setAttribute(key, value);
                }
            }
        } else if (resultName.equals(Result.NAME_INVALIDATE_SESSION)) { //注销session
            request.getSession().invalidate();
        } else if(resultName.equals(Result.NAME_DOWNLOAD)) { //文件下载
            File file = (File)model.getObj("downloadFile_");
            String fileName = model.get("downloadFileName_");
            Objects.requireNonNull(file, "result name为download，model中必须含有downloadFile_键值才能顺利工作");
            Objects.requireNonNull(fileName, "result name为download，model中必须含有downloadFileName_键值才能顺利工作");
            if(!file.exists()) {
                throw new IOException("文件不存在");
            }
            response.setHeader("Content-Disposition","attachment;filename*=utf-8'zh_cn'"+URLEncoder.encode(fileName, "UTF-8"));
            try(FileInputStream fileIn = new FileInputStream(file);
                    ServletOutputStream servletOut = response.getOutputStream()) {
                IOUtils.copyStream(fileIn, servletOut);
            } catch(IOException e) {
                throw new IOException("文件下载失败", e);
            }
        }
    }
}
