package cn.edu.jxau.java.io;

import java.io.IOException;
import java.io.InputStream;

public class FPipedInputStream extends InputStream {
    
    private static final int DEF_PIPE_SIZE = 1024;
    
    private boolean closedByWriter;
    private boolean closedByReader;
    private Thread readerSide; //读取管道数据的线程
    private Thread writerSide; //写入管道数据的线程
    private byte[] buff;
    
    int in;
    int out;
    boolean connected;
    
    public FPipedInputStream() {
        initPipe(DEF_PIPE_SIZE);
    }
    
    public FPipedInputStream(int size) {
        initPipe(size);
    }
    
    private void initPipe(int size) {
        if(size<=0) {
            throw new IllegalArgumentException(String.format("size=%d,参数size必须为正数",size));
        }
        buff = new byte[size];
    }
    
    /**
     * 写入线程向管道中写入数据
     * @throws IOException 
     */
    protected synchronized void receive(int b) throws IOException {
        
        checkPipeState();
        writerSide = Thread.currentThread();
        if(in == out) { //管道已满
            awaitSpace();
        }
        if(in<0) {
            in = 0;
            out = 0;
        }
        buff[in++] = (byte) (b&0xFF);
        if(in==buff.length) { //重绕缓冲区
            in = 0;
        }
        
    }
    
    /**
     * 判断管道的状态：是否连接、是否关闭、读取线程的状态
     * @throws IOException 
     */
    private void checkPipeState() throws IOException {
        
        if(!connected) {
            throw new IOException("管道未连接");
        } else if(closedByWriter || closedByReader) {
            throw new IOException("管道已关闭");
        } else if(readerSide!=null && !readerSide.isAlive()) {
            throw new IOException("读取线程已经销毁");
        }
    }
    
    /**
     * 当管道为空时则等待1s，然后再次判断，直到管道中的数据可以读取
     * @throws IOException 
     */
    private void awaitSpace() throws IOException {
        
        while(in==out) {
            checkPipeState();
            notifyAll();
            try {
                wait(1000);
            } catch(InterruptedException ex) {
                throw new RuntimeException("线程阻塞时被中断",ex);
            }
        }
    }
    
    synchronized void receiveLast() {
        
        closedByWriter = true;
        notifyAll();
    }
    
    @Override
    public synchronized int read() throws IOException {
        
        checkPipeState();
        readerSide = Thread.currentThread();
        while(in<0) {
            try {
                notifyAll();
                wait(1000);
            } catch (InterruptedException ex) {
                throw new java.io.InterruptedIOException();
            }
        }
        int ret = buff[out++] & 0xFF;
        if(out==buff.length) { //重绕缓冲区
            out = 0;
        }
        if(in==out) {
            in = -1;
        }
        return ret;
    }
    
    @Override
    public int read(byte[] toBuff,int off,int len) {
        throw new UnsupportedOperationException();
    }
    
    /**
     * 实质就是获取管道的空闲大小
     */
    public synchronized int available() throws IOException {
        
        if(in == out) { //管道为空
            return buff.length;
        } else if(in>out) {
            return in-out;
        } else if(out>in) {
            return in+buff.length-out;
        }
        return 0;
    }
    
    public synchronized void close() {
        closedByReader = true;
        in = -1;
    }
}
