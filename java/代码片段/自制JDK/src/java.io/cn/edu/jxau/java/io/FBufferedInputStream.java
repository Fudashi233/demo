package cn.edu.jxau.java.io;

import java.io.FileInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FBufferedInputStream extends FilterInputStream {

    public static void main(String[] args) throws IOException {
        
        FBufferedInputStream in = new FBufferedInputStream(
                new FileInputStream("C:\\Users\\PC-Clive\\Desktop\\test.txt"));
        for(int i=0;i<5;i++) {
            System.out.println((char)in.read());
        }
        if(!in.markSupported()) {
            throw new RuntimeException("该流不支持标记功能");
        }
        in.mark(1024);
        in.skip(22);
        for(int i=0;i<5;i++) {
            System.out.println((char)in.read());
        }
        in.reset();
        for(int i=0;i<5;i++) {
            System.out.println((char)in.read());
        }
        in.close();
    }
    
    private final static int DEF_BUFF_SIZE = 1024; // 默认缓冲区大小

    private byte[] buff;
    private int pos;
    private int count;
    private int markPos;
    private int markLimit;

    public FBufferedInputStream(InputStream in) {
        this(in, DEF_BUFF_SIZE);
    }

    public FBufferedInputStream(InputStream in, int size) {

        super(in);
        if (size <= 0) {
            throw new IllegalArgumentException("参数size必须是正数");
        }
        buff = new byte[size];
        markPos = -1; // 初始时无标记位
    }

    private InputStream getInIfOpen() throws IOException {

        if (in == null) {
            throw new IOException("in is null,stream closed");
        }
        return in;
    }

    private byte[] getBuffIfOpen() throws IOException {

        if (buff == null) {
            throw new IOException("buff is null,stream closed");
        }
        return buff;
    }

    /**
     * 填充缓冲区
     */
    private void fill() throws IOException {

        if (buff == null || in == null) {
            throw new IOException("stream closed");
        }
        if (markPos < 0) {
            // 1.缓冲区中无标记位
            pos = 0;
        } else if (pos >= buff.length) {
            if (markPos > 0) {
                // 2.缓冲区中没有剩余空间 && 标记位大于0
                int size = pos - markPos;
                System.arraycopy(buff, markPos, buff, 0, size); // 将标志位后面的数据前移
                pos = size;
                markPos = 0;
            } else if (buff.length >= markLimit) {
                // 3.缓冲区中没有剩余空间 && 标记位等于0 && buffer.length>=marklimit
                markPos = -1;
                pos = 0;
            } else {// 扩大缓冲区
                // 4.缓冲区中没有剩余空间 && 标记位等于0 && buffer.length<marklimit
                int nSize = pos * 2;// new size
                nSize = nSize>markLimit?markLimit:nSize;
                byte[] nBuff = new byte[nSize];// new buffer
                System.arraycopy(buff,0,nBuff,0,pos);
                buff = nBuff;
            }
        }
        count = pos;
        int n = getInIfOpen().read(buff, pos, buff.length - pos);
        if (n > 0) {
            count = n + pos;
        }
    }

    @Override
    public synchronized int read() throws IOException {

        if (pos >= count) { // 缓冲区已空
            fill();
            if (pos >= count) { // 缓冲区仍然为空，说明字节流中已没有数据
                return -1;
            }
        }
        return (int) (getBuffIfOpen()[pos++] & 0xFF);
    }

    @Override
    public synchronized int read(byte[] toBuff, int off, int len) throws IOException {

        // 参数校验 //
        if (off < 0) {
            throw new IllegalArgumentException("off必须是非负数");
        }
        if (len <= 0) {
            throw new IllegalArgumentException("len必须是正数");
        }
        if (len < toBuff.length - off) {
            throw new IllegalArgumentException(String.format("若从%d开始，toBuff中并不包含%d个字节", off, len));
        }
        if (in == null || buff == null) {
            throw new IOException("stream closed");
        }

        // 读取数据 //
        int n = 0;
        for (;;) {
            int nRead = read1(toBuff, off + n, len - n);
            if (nRead <= 0) {
                return n == 0 ? nRead : n;
            }
            n += nRead;
            if (n >= len) { // 读取完毕
                return n;
            }
            if (in != null && in.available() <= 0) {
                return n;
            }
        }
    }

    private int read1(byte[] toBuff, int off, int len) throws IOException {

        int avail = count - pos;
        if (avail <= 0) {
            if (len >= getBuffIfOpen().length && markPos < 0) {
                // avail小于等于0，说明缓冲区为空，直接从
                // 字节流中读取加速读取速度
                return in.read(toBuff, off, len);
            }
            fill();
            avail = count - pos;
            if (avail <= 0) {
                return -1;
            }
        }
        int cnt = Math.min(avail, len);
        System.arraycopy(getBuffIfOpen(), pos, toBuff, off, cnt);
        pos += cnt; // 移动pos索引
        return cnt;
    }

    @Override
    public synchronized long skip(long n) throws IOException {

        // 参数校验 //
        if (in == null || buff == null) {
            throw new IOException("stream closed");
        }

        // 特殊情况处理 //
        if (n <= 0) {
            return 0;
        }

        int avail = count - pos;
        if (avail <= 0) {

            if (markPos < 0) {
                return getInIfOpen().skip(n);
            }
            fill();
            avail = count - pos;
            if (avail <= 0) { // 填充缓冲区后，avail仍然小于等于零，说明字节输入流已空
                return 0;
            }
        }
        long skip = Math.min(avail, n);
        pos += skip;
        return skip;
    }

    @Override
    public synchronized int available() throws IOException {

        int availInBuff = count - pos;
        int availInStream = getInIfOpen().available();

        // 如果availInBuff+availInStream大于Integer.MAX_VALUE
        // 则返回Integer.MAX_VALUE，否则就返回availInBuff+availInStream
        return availInBuff > Integer.MAX_VALUE - availInStream ? Integer.MAX_VALUE : availInBuff + availInStream;
    }

    @Override
    public synchronized void mark(int markLimit) {

        this.markLimit = markLimit;
        markPos = pos;
    }

    @Override
    public boolean markSupported() {
        return true;
    }

    public synchronized void reset() throws IOException {

        // 参数校验 //
        if (buff == null || in == null) {
            throw new IOException("stream closed");
        }
        if (markPos < 0) {
            throw new IllegalArgumentException("标志位必须是非负数");
        }

        pos = markPos;
    }

    public void close() throws IOException {

        if (buff != null) {
            buff = null; // 置空，游离对象
        }
        if (in != null) {
            in.close();
        }
    }
}
