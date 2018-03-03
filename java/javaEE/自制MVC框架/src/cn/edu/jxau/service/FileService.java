package cn.edu.jxau.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.edu.jxau.controler.core.Model;
import cn.edu.jxau.controler.core.Result;
import cn.edu.jxau.util.IOUtils;

public class FileService {

    private FileService() {
        throw new UnsupportedOperationException("不可实例化FileService");
    }

    public static String download(Model model) throws IOException {
        
        String fileName = model.get("fileName");
        File webInfDir = new File(Thread.currentThread().getContextClassLoader().getResource("").getPath()).getParentFile();
        File uploadDir = new File(webInfDir, "upload");
        File file = new File(uploadDir, fileName);
        String realFileName = fileName.substring(fileName.lastIndexOf("_")+1);
        model.put("downloadFile_",file);
        model.put("downloadFileName_",realFileName);
        return Result.NAME_DOWNLOAD;
    }

    public static String queryFiles(Model model) {
        
        File webInfDir = new File(Thread.currentThread().getContextClassLoader().getResource("").getPath()).getParentFile();
        File uploadDir = new File(webInfDir, "upload");
        File[] files = uploadDir.listFiles();
        if(files==null) {
            model.put("message","无法获取upload目录的文件");
            return Result.NAME_FAILURE;
        }
        Map<String,String> fileNameMap = new HashMap<>();
        for(File f : files) {
            String fileName = f.getName();
            String realFileName = fileName.substring(fileName.lastIndexOf("_")+1);
            fileNameMap.put(fileName,realFileName);
        }
        model.put("fileNameMap",fileNameMap);
        return Result.NAME_SUCCESS;
    }

    public static String upload(Model model) throws IOException {
        
        File webInfDir = new File(Thread.currentThread().getContextClassLoader().getResource("").getPath()).getParentFile();
        File file = (File)((Object[])model.getObj("file1"))[0];
        System.out.println(file.getAbsolutePath());
        System.out.println(new File(webInfDir,"upload").getAbsolutePath());
        try(FileInputStream fileIn = new FileInputStream(file);
            FileOutputStream fileOut = new FileOutputStream(new File(webInfDir,"upload"+File.separator+file.getName()));){
            IOUtils.copyStream(fileIn, fileOut);
        } catch(IOException e) {
            throw new IOException("文件复制失败",e);
        }
        return Result.NAME_SUCCESS;
    }
}
