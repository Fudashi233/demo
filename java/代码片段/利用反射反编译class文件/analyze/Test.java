package analyze;

import java.io.IOException;
import java.lang.reflect.Method;

public class Test {
    
    public static void main(String[] args) throws IOException {
        
        new AnalyzeClass(Student.class).getSrc("C:/Users/PC-Clive/Desktop/test.java");
    }
}