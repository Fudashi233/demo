package cn.edu.jxau.test;

import java.util.Date;

public class Request {
    
    private String IP; //IP地址
    private Date date; //请求发起时间
    
    public Request(String IP) {
        this.IP = IP;
        this.date = new Date();
    }
    
    public Request(String IP, Date date) {
        
        this.IP = IP;
        this.date = date;
    }
    
    public String getIP() {
        return IP;
    }
    public void setIP(String iP) {
        IP = iP;
    }
    
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    
    @Override
    public String toString() {
        return "Request [IP=" + IP + ", date=" + date + "]";
    }
}
