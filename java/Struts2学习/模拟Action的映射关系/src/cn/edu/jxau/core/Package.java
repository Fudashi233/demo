package cn.edu.jxau.core;

import java.util.Map;

public class Package {
    
    private String name;
    private String namespce = "/";
    private Map<String,Action> actionMap; //key为action的name，value为action对象
    
    public Package() {
        
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamespce() {
        return namespce;
    }

    public void setNamespce(String namespce) {
        this.namespce = namespce;
    }

    public Map<String, Action> getActionMap() {
        return actionMap;
    }

    public void setActionMap(Map<String, Action> actionMap) {
        this.actionMap = actionMap;
    }

    @Override
    public String toString() {
        return "Package [name=" + name + ", namespce=" + namespce + "]";
    }
}
