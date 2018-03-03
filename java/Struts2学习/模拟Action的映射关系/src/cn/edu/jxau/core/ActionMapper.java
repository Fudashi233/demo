package cn.edu.jxau.core;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import cn.edu.jxau.utils.XMLUtil;

public class ActionMapper {

    private Map<String, Package> packageMap; // package的name作为key，package对象作为value
    private static final String CONFIG_FILE_NAME = "actions.xml";
    public ActionMapper(String configPath) throws DocumentException {
        
        configPath = Thread.currentThread().getContextClassLoader().getResource(configPath).getPath();
        System.out.println(configPath);
        packageMap = new HashMap<>();
        init(new File(configPath+File.separator+CONFIG_FILE_NAME));
    }

    /**
     * 加载action的配置文件
     * @throws IOException 
     * @throws DocumentException 
     */
    private void init(File configFile) throws DocumentException {

        // 参数校验 //
        Objects.requireNonNull(configFile, "参数actionsFile不能为null");

        // 加载actions配置文件 //
        Document document = null;
        try {
            document = XMLUtil.parse(configFile);
        } catch (DocumentException e) {
            throw new DocumentException(String.format("文件%s加载失败", configFile.getAbsoluteFile()), e);
        }

        // 加载package信息 //
        Element framework = document.getRootElement();
        Iterator<Element> packageIterator = framework.elements("package").iterator();
        while (packageIterator.hasNext()) {

            Element packageElement = packageIterator.next();
            String name = packageElement.attributeValue("name");
            String namespace = packageElement.attributeValue("namespace");
            Package packageInstance = new Package();
            if (name == null || name.trim().equals("")) { // 设置name属性
                throw new RuntimeException("<package>配置错误,缺少name属性或name属性值不能为空");
            } else {
                packageInstance.setName(name);
            }
            if (namespace != null) { // 设置namespace属性
                if (namespace.trim().equals("")) {
                    throw new RuntimeException("<package>配置错误,namespace属性值不能为空");
                } else {
                    packageInstance.setNamespce(namespace);
                }
            }
            packageInstance.setActionMap(getActionMap(packageElement.elements("action").iterator()));
            packageMap.put(packageInstance.getName(), packageInstance);
        }
    }

    private Map<String, Action> getActionMap(Iterator<Element> actionIterator) {
        
        Objects.requireNonNull(actionIterator, "参数actionIterator不能为null");
        Map<String, Action> actionMap = new HashMap<>();
        while (actionIterator.hasNext()) {
            
            Element actionElement = actionIterator.next();
            Action action = new Action();
            String name = actionElement.attributeValue("name");
            String clazz = actionElement.attributeValue("class");
            String method = actionElement.attributeValue("method");
            if (name == null || name.trim().equals("")) { // 设置name属性
                throw new RuntimeException("<action>配置错误,缺少name属性或者name属性不能为空");
            } else {
                action.setName(name);
            }
            if (name == null || name.trim().equals("")) { // 设置clazz属性
                throw new RuntimeException("<action>配置错误,缺少class属性或者class属性不能为空");
            } else {
                action.setClazz(clazz);
            }
            if (method != null) { // 设置method属性
                if (method.trim().equals("")) {
                    throw new RuntimeException("<action>配置错误,method属性不能为空");
                } else {
                    action.setMethod(method);
                }
            }
            action.setResultMap(getResultMap(actionElement.elements("result").iterator()));
            actionMap.put(action.getName(),action);
        }
        return actionMap;
    }

    private Map<String, Result> getResultMap(Iterator<Element> resultIterator) {
        
        Objects.requireNonNull(resultIterator, "参数resultIterator不能为null");
        Map<String,Result> resultMap = new HashMap<>();
        while(resultIterator.hasNext()) {
            
            Element resultElement = resultIterator.next();
            Result result = new Result();
            String name = resultElement.attributeValue("name");
            String type = resultElement.attributeValue("type");
            String location = resultElement.getText();
            if (name != null) { // 设置name属性
                if (name.trim().equals("")) {
                    throw new RuntimeException("<result>配置错误,name属性不能为空");
                } else {
                    result.setName(name);
                }
            }
            if (type != null) { // 设置type属性
                if (type.trim().equals("")) {
                    throw new RuntimeException("<result>配置错误,type属性不能为空");
                } else {
                    result.setType(type);
                }
            }
            if (location == null || location.trim().equals("")) { // 设置location属性
                throw new RuntimeException("<result>配置错误,缺少location属性或者location属性不能为空");
            } else {
                result.setLocation(location);
            }
            resultMap.put(result.getName(),result);
        }
        return resultMap;
    }
    
    
    /**
     * 根据url获取对应的Action
     * @param contextPath
     * @param url
     * @return
     */
    public Action getAction(String contextPath,String url) {
        
        // 获取action名称 //
        String namespace = url.substring(url.indexOf(contextPath)+contextPath.length(),url.lastIndexOf("/"));
        String actionName = url.substring(url.lastIndexOf("/")+1); //去除命名空间
        actionName = actionName.substring(0,actionName.indexOf(".action")); //去除.action后缀
        
        // 第一次遍历packageMap寻找完全对应的Action //
        Set<Entry<String, Package>> entrySet = packageMap.entrySet();
        Iterator<Entry<String,Package>> iterator1 = entrySet.iterator();
        while(iterator1.hasNext()) {
            Entry<String,Package> entry = iterator1.next();
            Package packageInstance = entry.getValue();
            if(namespace.equals(packageInstance.getNamespce())) {
                Action action = packageInstance.getActionMap().get(actionName);
                if(action != null) {
                    return action;
                }
            }
        }
        
        // 第二次遍历packageMap，到默认的namespace下寻找匹配的action //
        Iterator<Entry<String,Package>> iterator2 = entrySet.iterator();
        while(iterator2.hasNext()) {
            
            Entry<String,Package> entry = iterator2.next();
            Package packageInstance = entry.getValue();
            if(packageInstance.getNamespce().equals("/")) { //package的namespace是默认值
                Action action = packageInstance.getActionMap().get(actionName);
                if(action != null) {
                    return action;
                }
            }
        }
        return null; //未找到匹配的action
    }
    
    /**
     * 根据url获取对应的Action
     * @param request
     * @return
     */
    public Action getAction(HttpServletRequest request) {
        return getAction(request.getContextPath(),request.getRequestURI());
    }
}
