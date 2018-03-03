package cn.edu.jxau.controler.core;

import java.util.List;
import java.util.Map;

public class Service {

    private String name;
    private String method;
    private String clazz;
    private String power;
    private Map<String, Result> resultMap;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getMethod() {
        return method;
    }
    
    public void setMethod(String method) {
        this.method = method;
    }
    
    public String getClazz() {
        return clazz;
    }
    
    public void setClazz(String clazz) {
        this.clazz = clazz;
    }
    
    public String getPower() {
        return power;
    }
    
    public void setPower(String power) {
        this.power = power;
    }
    
    public Map<String, Result> getResultMap() {
        return resultMap;
    }

    public void setResultMap(Map<String, Result> resultMap) {
        this.resultMap = resultMap;
    }

    @Override
    public String toString() {
        return "Service [name=" + name + ", method=" + method + ", clazz=" + clazz + ", power=" + power + ", resultMap="
                + resultMap + "]";
    }
}
