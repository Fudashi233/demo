package cn.edu.jxau.test;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;


/**
 * hack for windows,只能在windows下工作
 * @author 付大石
 */
public class LineOutputStream extends FilterOutputStream {

    private int curLine; //当前行号
    private final String LINE_HEAD_FORMAT;
    private int preByte;
    public LineOutputStream(OutputStream out,String lineFormat) throws IOException {
        
        super(out);
        curLine = 1;
        LINE_HEAD_FORMAT = lineFormat;
        newLine(); //初始行号
    }
    
    /**
     * 写入换行符及下一行的行号
     * @throws IOException 
     */
    public void newLine() throws IOException {
        
        super.write(String.format(LINE_HEAD_FORMAT,curLine).getBytes());
        curLine++;
    }
    
    
    @Override
    public void write(int b) throws IOException {
        
        super.write(b);
        if(isNewLine(b)) {
            newLine();
        }
        preByte = b;
    }
    
    /**
     * 判断输出流是否换行了
     * @param curByte
     * @return
     */
    public boolean isNewLine(int curByte) {
        
        return preByte==13 && curByte==10; //windows下，文件的换行符是13、10
    }
}
