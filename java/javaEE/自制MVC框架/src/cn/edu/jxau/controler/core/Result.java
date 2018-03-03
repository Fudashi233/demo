package cn.edu.jxau.controler.core;

public class Result {

    /**
     * <result />标签的type属性
     */
    public static final String TYPE_DISPATCHER = "dispatcher";
    public static final String TYPE_REDIRECT= "redirect";
    
    /**
     * <result />标签的name属性
     */
    public static final String NAME_SUCCESS = "success";
    public static final String NAME_FAILURE = "failure";
    public static final String NAME_UPDATE_SESSION = "updateSession";
    public static final String NAME_INVALIDATE_SESSION = "invalidateSession";
    public static final String NAME_DOWNLOAD = "download";
    
    private String name = NAME_SUCCESS;
    private String type = TYPE_DISPATCHER;
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
