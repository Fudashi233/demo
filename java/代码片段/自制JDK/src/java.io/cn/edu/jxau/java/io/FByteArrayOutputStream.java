package cn.edu.jxau.java.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Objects;

public class FByteArrayOutputStream extends OutputStream {
    
    public static void main(String[] args) throws IOException {
        
        FByteArrayOutputStream byteArrayOut = new FByteArrayOutputStream();
        
        // t1：字节写入 //
        byteArrayOut.write('a'); //单字节写入
        byteArrayOut.write('b');
        byteArrayOut.write('c');
        System.out.printf("size:%d,buf:%s\n",byteArrayOut.size(),byteArrayOut);
        byteArrayOut.write(new byte[]{'d','e','f','g','h','i'},2,4);//多字节写入
        System.out.printf("size:%d,buf:%s\n",byteArrayOut.size(),byteArrayOut);
        
        // t2:将buf写入到另一个输出流中 //
        FByteArrayOutputStream byteArrayOutX = new FByteArrayOutputStream();
        byteArrayOut.writeTo(byteArrayOutX);
        System.out.println(byteArrayOutX);
    }
    
    // ========================================
    // 数据域
    // ========================================
    private byte buf[];
    private int count;
    
    // ========================================
    // 构造函数
    // ========================================
    public FByteArrayOutputStream() {
        this(32);
    }
    
    public FByteArrayOutputStream(int size) {
        
        if(size<0) {
            throw new IllegalArgumentException("参数size不能为负数");
        }
        buf = new byte[size];
        count = 0;
    }
    
    private void ensureCapacity(int minCapacity) {
        
        if(minCapacity<0) {
            throw new IllegalArgumentException("参数minCapacity不能为负数");
        }
        if(minCapacity>buf.length) {
            grow(minCapacity);
        }
    }
    
    private void grow(int minCapacity) {
        
        if(minCapacity<0) {
            throw new IllegalArgumentException("参数minCapacity不能为负数");
        }
        int newCapacity = buf.length<<1;
        newCapacity = Math.max(minCapacity,newCapacity); //取两者中较大的一个作为新的容量
        buf = Arrays.copyOf(buf,newCapacity); //复制并扩容
    }
    
    @Override
    public synchronized void write(int b) {
        
        ensureCapacity(count+1);
        buf[count] = (byte)b;
        count++;
    }
    
    @Override
    public synchronized void write(byte[] fromBuf,int off,int len) {
        
        // 参数校验 //
        Objects.requireNonNull(fromBuf,"参数fromBuf不能为null");
        if(off<0) {
            throw new IllegalArgumentException("参数off不能为负数");
        }
        if(len<0) {
            throw new IllegalArgumentException("参数len不能为负数");
        }
        if(len>fromBuf.length-off) {
            throw new IllegalArgumentException(String.format("若从%d开始，fromBuf中并不包含%d个字节",off,len));
        }
        ensureCapacity(count+len);
        System.arraycopy(fromBuf,off,buf,count,len);
        count += len;
    }
    
    public synchronized void writeTo(OutputStream out) throws IOException {
        out.write(buf,0,count);
    }
    
    public synchronized void reset() {
        count = 0;
    }
    
    public synchronized byte[] toByteArray() {
        
        byte[] result = new byte[count];
        for(int i=0;i<result.length;i++) {
            result[i] = buf[i];
        }
        return result;
    }
    
    public synchronized int size() {
        return count;
    }
    
    @Override
    public synchronized String toString() {
        return new String(buf,0,count);
    }
    
    public synchronized String toString(String charset) throws UnsupportedEncodingException {
        return new String(buf,0,count,charset);
    }
    
    @Override
    public void close() {
        //nothing to do
    }
}
