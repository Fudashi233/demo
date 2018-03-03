package cn.edu.jxau.java.io;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

public class FPipedOutputStream extends OutputStream {
    
    private FPipedInputStream sink;
    
    public FPipedOutputStream() {
        
    }
    
    public synchronized void connect(FPipedInputStream sink) {
        
        this.sink = sink;
        sink.in = -1; //管道输入流写入的位置
        sink.out = 0; //管道输入写读出的位置
        sink.connected = true; //管道输入流是否连接
    }

    @Override
    public void write(int b) throws IOException {
        
        Objects.requireNonNull(sink,"管道未建立连接");
        sink.receive(b);
    }
    
    @Override
    public void write(byte[] fromBuf,int off,int len) {
        throw new UnsupportedOperationException();
    }
    
    public synchronized void flush() {
        
        if(sink != null) {
            synchronized(sink) {
                sink.notifyAll();
            }
        }
    }
    
    public void close() throws IOException {
        
        if(sink != null) {
            flush();
            sink.receiveLast();
        }
    }
}
