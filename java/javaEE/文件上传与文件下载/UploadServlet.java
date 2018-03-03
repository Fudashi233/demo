package cn.edu.jxau.controler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import cn.edu.jxau.util.ConfigManager;
import cn.edu.jxau.util.IOUtils;

@WebServlet("/uploadServlet")
public class UploadServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 获取并构造上传路径 //
        String uploadPath = super.getServletContext().getRealPath("/WEB-INF/upload"); // 文件上传路径
        File uploadDir = new File(uploadPath);
        if(uploadDir.exists()) {
            if(!uploadDir.isDirectory()) {
                throw new IOException("路径存在，但指定的路径不是文件夹");
            }
        } else {
            if(!uploadDir.mkdirs()) {
                throw new IOException("路径不存在，且尝试创建时失败");
            }
        }

        // 上传文件 //
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(Integer.parseInt(ConfigManager.getConfig("sizeThreshold"))); //设置缓存大小
        factory.setRepository(ConfigManager.tmpDir());
        System.out.println(ConfigManager.tmpDir().getAbsolutePath());
        ServletFileUpload fileUpload = new ServletFileUpload(factory);
        fileUpload.setHeaderEncoding(ConfigManager.getConfig("encoding"));
        if (!ServletFileUpload.isMultipartContent(request)) {
            throw new RuntimeException("no multipart content");
        }
        try {
            List<FileItem> list = fileUpload.parseRequest(request);
            Iterator<FileItem> iterator = list.iterator();
            while (iterator.hasNext()) {
                FileItem fileItem = iterator.next();
                if (fileItem.isFormField()) { // 表单的普通参数
                    String name = fileItem.getFieldName();
                    String value = fileItem.getString(ConfigManager.getConfig("encoding"));
                } else { // 表单的文件参数
                    String fileName = fileItem.getName();
                    if (fileName == null || fileName.trim().equals("")) {
                        continue ;
                    }
                    
                    // 获取文件名//
                    fileName = fileName.substring(fileName.lastIndexOf(File.separator)+1);
                    String newFileName = getUUIDName(fileName); //获取唯一的文件名
                    File uploadFile = getUploadFile(uploadDir,newFileName);
                    System.out.println("上传文件目标路径："+uploadFile.getAbsolutePath());
                    System.out.println("上传文件名称"+fileName);
                    System.out.println("上传文件contentType："+fileItem.getContentType());
                    
                    // 将文件数据从FileItem的流中读取到指定位置  //
                    try(InputStream in = fileItem.getInputStream();
                            FileOutputStream out = new FileOutputStream(uploadFile)) {
                        IOUtils.copyStream(in, out);
                    } catch(IOException e) {
                        throw new IOException(String.format("文件(%s)复制失败",fileName), e);
                    } finally {
                        fileItem.delete();
                    }
                }
            }
        } catch (FileUploadException e) {
            request.setAttribute("message", "文件上传失败");
            throw new RuntimeException("文件上传失败", e);
        }
        request.setAttribute("message", "文件上传成功");
        request.getRequestDispatcher("/message.jsp").forward(request, response);
    }
    
    private String getUUIDName(String fileName) {
        return String.format("%s_%s",UUID.randomUUID().toString(),fileName);
    }
    
    /**
     * 利用文件名的hashCode，打乱文件的存储，防止一个文件夹下聚集太多的文件
     * @param uploadDir
     * @param fileName
     * @return
     * @throws IOException
     */
    private File getUploadFile(File uploadDir, String fileName) throws IOException {
        
        int hashCode = fileName.hashCode();
        int dir1 = hashCode & 0xF;
        int dir2 = (hashCode & 0xF0) >> 4;
        String hashPath = dir1+File.separator+dir2+File.separator+fileName;
        File file = new File(uploadDir, hashPath);
        File parent = file.getParentFile();
        if(!parent.exists()) {
            if(!parent.mkdirs()) {
                throw new IOException("父文件夹创建失败");
            }
        }
        return file;
    }
}
