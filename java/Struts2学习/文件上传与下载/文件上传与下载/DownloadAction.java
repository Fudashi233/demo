package cn.edu.jxau.action;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

import cn.edu.jxau.util.ObjectUtils;

public class DownloadAction extends ActionSupport {

    private String inputPath;
    private String contentType;
    private String fileName;

    public DownloadAction() {
        
    }
    
    public InputStream getTargetFile() throws IllegalArgumentException, IllegalAccessException {
        ObjectUtils.printFields(this);
        return ServletActionContext.getServletContext().getResourceAsStream(inputPath);
    }
    
    public String getInputPath() {
        return inputPath;
    }

    public void setInputPath(String inputPath) throws UnsupportedEncodingException {
        this.inputPath = new String(inputPath.getBytes("iso-8859-1"),"UTF-8");
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) throws UnsupportedEncodingException {
        this.fileName = new String(fileName.getBytes("iso-8859-1"),"UTF-8");
    }
}
