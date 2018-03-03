package cn.edu.jxau.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Objects;

public class Test {
    
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        
        WeatherData weatherData = new WeatherData();
        GeneralDisplay display1 = new GeneralDisplay(weatherData);
        ForecastDisplay display2 = new ForecastDisplay(weatherData);
        weatherData.measure(12.2F,8F,5.7F);
        display1.display();
        display2.display();
        weatherData.measure(12.3F,9F,5.7F);
        display1.display();
        display2.display();
    }
    
    public static void delete(File path) throws IOException {
        
        Objects.requireNonNull(path, "参数path不能为null");
        if(!path.exists()) {
            throw new IOException(String.format("path指向的文件不存在,path.getCanonicalFile=%s",path.getCanonicalFile()));
        }
        if(path.isDirectory()) {
            deleteDirectory(path);
        } else {
            doDelete(path);
        }
    }
    
    private static void doDelete(File file) throws IOException {
        file.delete();
    }
    
    private static void deleteDirectory(File directory) throws IOException {
        
        if(!directory.isDirectory()) {
            throw new IOException("参数directory必须指向文件夹");
        }
        File[] fileArr = directory.listFiles();
        if(fileArr == null || fileArr.length == 0) {
            doDelete(directory);
        }
        for(File file : fileArr) {
            if (file.isDirectory()) {
                deleteDirectory(file);
            } else {
                doDelete(file);
            }
        }
        doDelete(directory);
    }
}
