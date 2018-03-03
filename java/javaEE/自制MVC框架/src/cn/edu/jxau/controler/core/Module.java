package cn.edu.jxau.controler.core;

import java.util.Map;

public class Module {
    
    private String name;
    private String namespace="/";
    private Map<String,Service> serviceMap;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getNamespace() {
        return namespace;
    }
    
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
    
    public Map<String, Service> getServiceMap() {
        return serviceMap;
    }
    
    public void setServiceMap(Map<String, Service> serviceMap) {
        this.serviceMap = serviceMap;
    }
    
    @Override
    public String toString() {
        return "Module [name=" + name + ", namespace=" + namespace + ", serviceMap=" + serviceMap + "]";
    }
}
