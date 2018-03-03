package test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MyClassLoader extends ClassLoader {
    
    private String path; //root path
    
    public MyClassLoader(String path) {
        this.path = path;
    }
    
    @Override
    protected Class<?> findClass(String className) {
        
        File classFile = getClassFile(className);
        try(FileInputStream in = new FileInputStream(classFile);
            ByteArrayOutputStream out = new ByteArrayOutputStream()){ //try-with-resource
            int temp = -1;
            while((temp=in.read())!=-1) {
                out.write(temp);
            }
            byte[] classData = out.toByteArray();
            return defineClass(className,classData,0,classData.length);
        } catch (IOException e) {
            throw new RuntimeException("字节码文件读取失败");
        }
    }
    
    /**
     * 根据类的全限定名和根路径{@code path}，获取字节码文件
     * @param className
     * @return
     */
    private File getClassFile(String className) {
        
        File file = new File(path,className.replace(".",File.separator)+".class");
        return file;
    }
}
