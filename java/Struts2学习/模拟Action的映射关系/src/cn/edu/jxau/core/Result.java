package cn.edu.jxau.core;

public class Result {
    
    private String name = "success";
    private String type = "dispatcher";
    private String location;
    
    public Result() {
        
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Result [name=" + name + ", type=" + type + ", location=" + location + "]";
    }
}
