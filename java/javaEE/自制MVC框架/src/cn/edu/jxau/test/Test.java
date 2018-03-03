package cn.edu.jxau.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Test {
    
    public static void main(String[] args) throws IOException {
        
        String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        File file = new File(path);
        System.out.println(file.getAbsolutePath());
   }
}
