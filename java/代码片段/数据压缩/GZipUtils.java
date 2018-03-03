package cn.ed.jxau.nio;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 使用GZIP进行简单压缩
 * @author 付大石
 */
public class GZipUtils {

    /**
     * 读写缓冲区大小
     */
    private static final int BUFFER_LEN = 1024;

    /**
     * 压缩文件后缀
     */
    private static final String EXT = ".gz";
    
    /**
     * end of file
     */
    private static final int EOF = -1;
    
    public GZipUtils() {
        throw new UnsupportedOperationException("不可实例化GZipUtils");
    }
    
    // --------------------------------------
    // compress method
    // --------------------------------------
    /**
     * 压缩数据流
     * @param data
     * @return
     * @throws IOException
     */
    public static byte[] compress(byte[] data) throws IOException {

        // 参数校验 //
        Objects.requireNonNull(data, "参数data不能为null");

        // 压缩数据 //
        try (ByteArrayInputStream in = new ByteArrayInputStream(data);
                ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            compress(in, out);
            return out.toByteArray();
        }
    }

    /**
     * 压缩文件，源文件被压缩后删除
     * @param path
     * @throws IOException
     */
    public static File compress(String path) throws IOException {
        return compress(path, true);
    }

    /**
     * 压缩文件
     * @param file
     * @param delete 压缩完成后是否删除原文件
     * @throws IOException
     */
    public static File compress(String path, boolean delete) throws IOException {
        return compress(new File(path), delete);
    }

    /**
     * 压缩文件，源文件被压缩后删除
     * @param file
     * @throws IOException
     */
    public static File compress(File file) throws IOException {
        return compress(file, true);
    }

    /**
     * 压缩文件
     * @param file
     * @param delete 压缩完成后是否删除原文件
     * @throws IOException
     */
    public static File compress(File file, boolean delete) throws IOException {

        // 参数校验 //
        Objects.requireNonNull(file, "参数file不能为null");
        if (file.isDirectory()) {
            throw new IOException(String.format("文件不可以是文件夹，文件路径：%s", file.getCanonicalPath()));
        }
        if (!file.exists()) { // 文件是否存在
            throw new IOException(String.format("文件不存在，文件路径：%s", file.getCanonicalPath()));
        }
        if (!file.canRead()) { // 文件是否可读
            throw new IOException(String.format("文件不可读，文件路口：%s", file.getCanonicalPath()));
        }

        // 压缩文件 //
        String fileName = file.getName().substring(0, file.getName().lastIndexOf("."));
        File compressFile = new File(file.getParentFile(), fileName + EXT); // 压缩后的文件路径
        try (FileInputStream in = new FileInputStream(file);
                FileOutputStream out = new FileOutputStream(compressFile)) {
            compress(in, out);
        } catch (IOException e) {
            throw new IOException("文件压缩失败", e);
        }
        if (delete) { // 是否删除文件
            file.delete();
        }
        return compressFile;
    }

    /**
     * 压缩IO流
     * @param in
     * @param out
     * @throws IOException
     */
    public static void compress(InputStream in, OutputStream out) throws IOException {

        // 参数校验 //
        Objects.requireNonNull(in, "参数in不能为null");
        Objects.requireNonNull(out, "参数out不能为null");

        if (!(out instanceof GZIPOutputStream)) {
            out = new GZIPOutputStream(out);
        }

        // 压缩IO流 //
        byte[] buff = new byte[BUFFER_LEN];
        int len = 0;
        while ((len = in.read(buff)) != EOF) {
            out.write(buff, 0, len);
        }
        ((GZIPOutputStream) out).finish();
        out.flush();
    }

    // ---------------------------------
    // decompress method
    // ---------------------------------
    /**
     * 压缩数据
     * @param data
     * @return
     * @throws IOException
     */
    public static byte[] decompress(byte[] data) throws IOException {

        try (ByteArrayInputStream in = new ByteArrayInputStream(data);
                ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            decompress(in, out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new IOException("数据解压失败", e);
        }
    }

    public static File decompress(String path) throws IOException {
        return decompress(path, true);
    }

    public static File decompress(String path, boolean delete) throws IOException {
        return decompress(new File(path), delete);
    }

    public static File decompress(File file) throws IOException {
        return decompress(file, true);
    }

    /**
     * 解压文件
     * @param file
     * @param delete 解压后，是否删除原文件
     * @throws IOException
     */
    public static File decompress(File file, boolean delete) throws IOException {

        // 参数校验 //
        Objects.requireNonNull(file, "参数file不能为null");
        if (file.isDirectory()) {
            throw new IOException(String.format("文件不能是文件夹,文件路径：%s", file.getCanonicalPath()));
        }
        if (!file.exists()) {
            throw new IOException(String.format("文件不存在,文件路径：%s", file.getCanonicalPath()));
        }
        if (!file.canRead()) {
            throw new IOException(String.format("文件不可读,文件路径：%s", file.getCanonicalPath()));
        }

        // 压缩文件 //
        String fileName = file.getName().substring(0, file.getName().lastIndexOf("."));
        File decompressFile = new File(file.getParentFile(), fileName);
        try (FileInputStream in = new FileInputStream(file);
                FileOutputStream out = new FileOutputStream(decompressFile)) {
            decompress(in, out);
        } catch (IOException e) {
            throw new IOException("文件解压失败", e);
        }
        if (delete) {
            file.delete();
        }
        return decompressFile;
    }

    /**
     * 解压IO流
     * @param in
     * @param out
     * @throws IOException
     */
    public static void decompress(InputStream in, OutputStream out) throws IOException {

        // 参数校验 //
        Objects.requireNonNull(in, "参数in不能为null");
        Objects.requireNonNull(out, "参数out不能为null");

        if (!(in instanceof GZIPInputStream)) {
            in = new GZIPInputStream(in);
        }

        // 解压IO流 //
        byte[] buff = new byte[BUFFER_LEN];
        int len = 0;
        while ((len = in.read(buff)) != EOF) {
            out.write(buff, 0, len);
        }
    }
}
