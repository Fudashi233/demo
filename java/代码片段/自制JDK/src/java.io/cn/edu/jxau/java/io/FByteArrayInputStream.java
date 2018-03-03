package cn.edu.jxau.java.io;

import java.io.InputStream;
import java.util.Objects;

public class FByteArrayInputStream extends InputStream {

    
    public static void main(String[] args) {
        
        byte[] buf = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q',
                'r','s','t','u','v','w','x','y','z'};
        FByteArrayInputStream byteArrayIn = new FByteArrayInputStream(buf);
        // t1：字节读取 //
//        for(int i=0;i<5;i++) { //读取5个字节
//            int temp = byteArrayIn.read();
//            if(temp<0) {
//                throw new RuntimeException("字节流已读取完毕");
//            } else {
//                System.out.println((char)temp);
//            }
//        }
//        for(int i=0;i<5;i++) {//再次读取5个字节
//            int temp = byteArrayIn.read();
//            if(temp<0) {
//                throw new RuntimeException("字节流已读取完毕");
//            } else {
//                System.out.println((char)temp);
//            }
//        }
        
        // t2:mark skip reset //
        byteArrayIn.mark(0);
        byteArrayIn.skip(5);
        for(int i=0;i<5;i++) {
            int temp = byteArrayIn.read();
            if(temp<0) {
                throw new RuntimeException("字节流已读取完毕");
            } else {
                System.out.println((char)temp);
            }
        }
        byteArrayIn.reset();
        for(int i=0;i<5;i++) {
            int temp = byteArrayIn.read();
            if(temp<0) {
                throw new RuntimeException("字节流已读取完毕");
            } else {
                System.out.println((char)temp);
            }
        }
    }
    
    // ========================================
    // 数据域
    // ========================================
    private byte[] buf;
    private int pos; //下一个被读取的字节索引
    private int mark; //标记位
    private int count; //字节流的长度
    
    // ========================================
    // 构造函数
    // ========================================
    public FByteArrayInputStream(byte[] buf) {
        
        Objects.requireNonNull(buf,"参数buf不能为null");
        this.buf = buf;
        this.pos = 0;
        this.count = buf.length;
        this.mark = 0;
    }
    
    public FByteArrayInputStream(byte[] buf,int off,int len) {
        
        // 参数校验 //
        Objects.requireNonNull(buf,"参数buf不能为null");
        if(off<0) {
            throw new IllegalArgumentException("参数offset不能为负数");
        }
        if(len<0) {
            throw new IllegalArgumentException("参数len不能为负数");
        }
        if(len > (buf.length-off)) {
            throw new IllegalArgumentException(String.format("若从%d开始，toBuf中并不包含%d个字节",off,len));
        }
        
        // 初始化数据域 //
        this.buf = buf;
        this.pos = off;
        this.count = off+len; //???
        this.mark = off;
    }
    
    
    // ========================================
    // 方法
    // ========================================
    
    /**
     * 从字节流中读取一个字节，如果字节流中已经不含有字节（pos>=count）就返回-1
     */
    @Override
    public synchronized int read() {
        return pos<count?(buf[pos++] & 0xFF):-1;
    }
    
    
    @Override
    public synchronized int read(byte[] toBuf,int off,int len) {
        
        // 参数校验 //
        Objects.requireNonNull(toBuf,"参数buf不能为null");
        if(off<0) {
            throw new IllegalArgumentException("参数offset不能为负数");
        }
        if(len<0) {
            throw new IllegalArgumentException("参数len不能为负数");
        }
        if(len > (toBuf.length-off)) {
            throw new IllegalArgumentException(String.format("若从%d开始，无法从字节流中读取%d个字节放入buf",off,len));
        }
        
        // 特殊情况处理 //
        if(pos>=count) { //字节流已读取完成
            return -1;
        }
        
        // 读取字节流 //
        int avail = count-pos;
        len = Math.min(avail,len);
        if(len==0) {
            return 0;
        }
        System.arraycopy(this.buf,pos,toBuf,off,len);
        pos+=len;
        return len;
    }
    
    @Override
    public synchronized long skip(long n) {
        
        // 参数校验 //
        if(n<0) {
            throw new IllegalArgumentException("参数n不能为负数");
        }
        
        // skip //
        long k = count-pos;
        if(n<k) {
            k = n;
        }
        pos += k;
        return k;
    }
    
    @Override
    public synchronized int available() {
        return count-pos;
    }
    
    @Override
    public boolean markSupported() {
        return true;
    }
    
    @Override
    public void mark(int i) {
        mark = pos;
    }
    
    @Override
    public synchronized void reset() {
        pos = mark;
    }
    
    @Override
    public void close() {
        //nothing to do
    }
}
