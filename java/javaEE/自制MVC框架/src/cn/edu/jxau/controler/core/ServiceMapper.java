package cn.edu.jxau.controler.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import cn.edu.jxau.util.ConfigManager;
import cn.edu.jxau.util.LogUtils;
import cn.edu.jxau.util.XMLUtils;

public class ServiceMapper {

    private static Map<String, Module> moduleMap;
    static {
        moduleMap = new HashMap<String, Module>();
        try {
            init(ConfigManager.getConfig("services"));
        } catch (DocumentException e) {
            throw new RuntimeException("配置文件加载失败", e);
        }
    }

    private static void init(String path) throws DocumentException {

        // 加载配置文件 //
        path = Thread.currentThread().getContextClassLoader().getResource(path).getPath();
        LogUtils.info("加载配置文件：" + path);
        Document document = XMLUtils.parse(path);

        // 加载module //
        Iterator<Element> moduleIterator = document.getRootElement().elementIterator("module");
        while (moduleIterator.hasNext()) {
            Element moduleEle = moduleIterator.next();
            Module module = new Module();
            
            // 设置name属性 //
            String name = moduleEle.attributeValue("name");
            if(name==null) {
                throw new RuntimeException("配置文件配置错误，<module />标签不能缺少name属性");
            }
            name = name.trim();
            if(name.equals("")) {
                throw new RuntimeException("配置文件配置错误，<module />标签的name属性值不能为空");
            }
            module.setName(name);
            
            // 设置namespace属性 //
            String namespace = moduleEle.attributeValue("namespace");
            if(namespace != null) {
                namespace = namespace.trim();
                if(namespace.equals("")) {
                    throw new RuntimeException("配置文件配置错误，<module />标签的namespace属性值不能为空");
                } else {
                    module.setNamespace(namespace);
                }
            }
            
            // 设置serviceMap属性 //
            module.setServiceMap(getServiceMap(moduleEle.elementIterator("service")));
            moduleMap.put(module.getName(), module);
        }
    }
    
    private static Map<String, Service> getServiceMap(Iterator<Element> serviceIterator) {
        
        Map<String,Service> serviceMap = new HashMap<>();
        while(serviceIterator.hasNext()) {
            Service service = getService(serviceIterator.next());
            serviceMap.put(service.getName(), service);
        }
        return serviceMap;
    }
    
    private static Service getService(Element serviceEle) {
        
        Service service = new Service();
        
        // 设置name属性 //
        String name = serviceEle.attributeValue("name");
        if(name == null) {
            throw new RuntimeException("配置文件配置错误，<service />标签不能缺少name属性");
        }
        name = name.trim();
        if(name.equals("")) {
            throw new RuntimeException("配置文件配置错误，<service />标签的name属性值不能为空");
        }
        service.setName(name);
        
        // 设置class属性 //
        String clazz = serviceEle.attributeValue("class");
        if(clazz == null) {
            throw new RuntimeException("配置文件配置错误，<service />标签不能缺少class属性");
        }
        clazz = clazz.trim();
        if(clazz.equals("")) {
            throw new RuntimeException("配置文件配置错误，<service />标签的class属性值不能为空");
        }
        service.setClazz(clazz);
        
        
        // 设置method属性 //
        String method = serviceEle.attributeValue("method");
        if(method == null) {
            throw new RuntimeException("配置文件配置错误，<service />标签不能缺少method属性");
        }
        method = method.trim();
        if(method.equals("")) {
            throw new RuntimeException("配置文件配置错误，<service />标签的method属性值不能为空");
        }
        service.setMethod(method);
        
        // 设置power属性 //
        String power = serviceEle.attributeValue("power");
        if(power != null) {
            power = power.trim();
            if(power.equals("")) {
                throw new RuntimeException("配置文件配置错误，<service />标签的power属性值不能为空");
            } else {
                service.setPower(power);
            }
        }
        
        // 设置resultMap属性 //
        service.setResultMap(getResultMap(serviceEle.elementIterator("result")));
        return service;
    }
    
    private static Map<String,Result> getResultMap(Iterator<Element> iterator) {
        
        Map<String, Result> resultMap = new HashMap<>();
        while(iterator.hasNext()) {
            Element resultEle = iterator.next();
            Result result = new Result();
            
            // 设置name //
            String name = resultEle.attributeValue("name");
            if(name != null) {
                name = name.trim();
                if(name.equals("")) {
                    throw new RuntimeException("配置文件配置错误，<result />标签的name属性值不能为空");
                } else {
                    result.setName(name);
                }
            }
            
            String type = resultEle.attributeValue("type");
            if(type != null) {
                type = type.trim();
                if(type.equals("")) {
                    throw new RuntimeException("配置文件配置错误，<result />标签的type属性值不能为空");
                } else {
                    result.setType(type);
                }
            }
            
            String location = resultEle.attributeValue("location");
            if(location != null) {
                location = location.trim();
                if(location.equals("")) {
                    throw new RuntimeException("配置文件配置错误，<result />标签的location属性值不能为空");
                } else {
                    result.setLocation(location);
                }
            } else {
                location = resultEle.getText().trim();
                if(location.equals("")) {
                    throw new RuntimeException("配置文件配置错误，<result />标签不能缺少location属性");
                }
                result.setLocation(location);
            }
            resultMap.put(result.getName(), result);
        }
        return resultMap;
    }
    
    public static Service getService(HttpServletRequest request) {
        return getService(request.getContextPath(), request.getRequestURI());
    }
    
    public static Service getService(String contextPath, String uri) {
        
        // 获取namespace和service的名称 //
        uri = uri.substring(uri.indexOf(contextPath)+contextPath.length());
        String namespace = uri.substring(0,uri.lastIndexOf("/"));
        if(namespace.length() == 0) {
            namespace = "/";
        }
        String service = uri.substring(uri.lastIndexOf("/")+1,uri.lastIndexOf("."));
        
        // 第一次遍历，寻找完全匹配的service //
        Iterator<Entry<String,Module>> iterator = moduleMap.entrySet().iterator();
        while(iterator.hasNext()) {
            Entry<String,Module> entry = iterator.next();
            Module module = entry.getValue();
            if(namespace.equals(module.getNamespace())) {
                return module.getServiceMap().get(service);
            }
        }
        
        // 第二次遍历，寻找默认命名空间下的service //
        iterator = moduleMap.entrySet().iterator();
        while(iterator.hasNext()) {
            Entry<String,Module> entry = iterator.next();
            Module module = entry.getValue();
            if("/".equals(module.getNamespace())) { //默认命名空间下的module
                Service s = module.getServiceMap().get(service);
                if(s!=null) {
                    return s;
                }
            }
        }
        return null;
    }
}
