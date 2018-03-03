package cn.edu.jxau.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.catalina.tribes.util.Arrays;
import org.apache.struts2.ServletActionContext;

import cn.edu.jxau.util.ObjectUtils;

public class UploadAction {

    private File[] img;
    private String[] imgContentType;
    private String[] imgFileName;
    private String savePath;
    private String allowTypes;

    public UploadAction() {

    }

    public String upload() throws IOException, IllegalArgumentException, IllegalAccessException {

        if (!allowType()) {
            System.out.println("类型不允许");
            return "input"; // 类型不允许
        }
        for (int i = 0; i < img.length; i++) {
            doUpload(img[i], imgFileName[i]);
        }
        return "success";
    }

    private void doUpload(File file, String fileName) throws IOException {

        FileInputStream input = null;
        FileOutputStream output = null;
        try {
            input = new FileInputStream(file);
            String path = ServletActionContext.getServletContext().getRealPath(savePath);
            output = new FileOutputStream(new File(new File(path), fileName));
            int temp = -1;
            while ((temp = input.read()) >= 0) {
                output.write(temp);
            }
        } catch (IOException ex) {
            throw new IOException("文件上传失败", ex);
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
            } catch (IOException ex) {
                throw new IOException("output关闭失败", ex);
            } finally {
                try {
                    if (input != null) {
                        input.close();
                    }
                } catch (IOException ex) {
                    throw new IOException("input关闭失败", ex);
                }
            }
        }
    }

    /**
     * 判断文件类型是否允许上传
     * @return
     */
    private boolean allowType() {

        String[] allowTypeArr = allowTypes.split(",");
        for (String contentType : imgContentType) {
            boolean in = false;
            for (String type : allowTypeArr) {
                if (contentType.equals(type)) {
                    in = true;
                }
            }
            if (!in) {
                return false;
            }
        }
        return true;
    }

    public File[] getImg() {
        return img;
    }

    public void setImg(File[] img) {
        this.img = img;
    }

    public String[] getImgContentType() {
        return imgContentType;
    }

    public void setImgContentType(String[] imgContentType) {
        this.imgContentType = imgContentType;
    }

    public String[] getImgFileName() {
        return imgFileName;
    }

    public void setImgFileName(String[] imgFileName) {
        this.imgFileName = imgFileName;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public String getAllowTypes() {
        return allowTypes;
    }

    public void setAllowTypes(String allowTypes) {
        this.allowTypes = allowTypes;
    }
}