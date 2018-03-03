package cn.edu.jxau.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

public class IOUtils {

    private IOUtils() {
        throw new UnsupportedOperationException("不可实例化工具类IOUtils");
    }

    public static void copyStream(InputStream in, OutputStream out) throws IOException {
        
        // 参数校验 //
        Objects.requireNonNull(in, "参数in不能为null");
        Objects.requireNonNull(out, "参数out不能为null");
        
        // 拷贝字节流 //
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = in.read(buffer)) > 0) {
            out.write(buffer,0,len);
        }
    }
}
