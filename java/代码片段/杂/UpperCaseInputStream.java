package cn.edu.jxau.test;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class UpperCaseInputStream extends FilterInputStream {
    
    public UpperCaseInputStream(InputStream inputStream) {
        super(inputStream);
    }
    
    
    @Override
    public int read() throws IOException {
        
        int c = super.read();
        if(c==-1) {
            return c;
        } else {
            return Character.toUpperCase((char)c);
        }
    }
    
    @Override
    public int read(byte[] byteArr,int offset,int len) throws IOException {
        
        int l = super.read(byteArr,offset,len);
        for(int i=offset;i<l;i++) {
            byteArr[i] = (byte) Character.toUpperCase((char)byteArr[i]);
        }
        return l;
    }
}
