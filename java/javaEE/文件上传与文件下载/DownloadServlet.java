package cn.edu.jxau.controler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.codec.binary.Base64;

import cn.edu.jxau.util.IOUtils;

@WebServlet("/downloadServlet")
public class DownloadServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String fileName = request.getParameter("fileName");
        String realFileName = fileName.substring(fileName.lastIndexOf("_") + 1);
        File file = getUploadFile(request.getServletContext().getRealPath("/WEB-INF/upload"), fileName);
        if (!file.exists()) {
            throw new IOException("文件不存在");
        }
        response.setHeader("Content-Disposition","attachment;filename*=utf-8'zh_cn'"+URLEncoder.encode(realFileName, "UTF-8"));
        try(FileInputStream fileIn = new FileInputStream(file);
                ServletOutputStream servletOut = response.getOutputStream()) {
            IOUtils.copyStream(fileIn, servletOut);
        } catch(IOException e) {
            throw new IOException("文件下载失败", e);
        }
    }

    private File getUploadFile(String uploadFile, String fileName) {

        int hashCode = fileName.hashCode();
        int dir1 = hashCode & 0xF;
        int dir2 = (hashCode & 0xF0) >> 4;
        return new File(uploadFile, dir1 + File.separator + dir2 + File.separator + fileName);
    }

}
