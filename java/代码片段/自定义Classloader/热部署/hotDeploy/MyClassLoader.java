package cn.edu.jxau.hotDeploy;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MyClassLoader extends ClassLoader {

    private File classFile;

    public MyClassLoader(String classFile) {
        this.classFile = new File(classFile);
    }

    @Override
    protected Class<?> findClass(String className) {

        try (FileInputStream fileInput = new FileInputStream(classFile);
                ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();) {
            int temp = -1;
            while ((temp = fileInput.read()) != -1) {
                byteOutput.write(temp);
            }
            byte[] classData = byteOutput.toByteArray();
            return defineClass(className, classData, 0, classData.length);
        } catch (IOException ex) {
            throw new RuntimeException("类加载失败", ex);
        }
    }
}
