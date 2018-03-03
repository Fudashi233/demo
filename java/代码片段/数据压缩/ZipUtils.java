package cn.ed.jxau.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Objects;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.CheckedOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 各种压缩算法的工具类
 * @author 付大石
 */
public class ZipUtils {

    /**
     * 读写缓冲区大小
     */
    private static final int BUFFER_LEN = 1024;

    /**
     * 压缩文件后缀
     */
    private static final String GZ_EXT = ".gz";
    private static final String ZIP_EXT = ".zip";
    private static final String ZLIB_EXT = ".zlib";

    /**
     * end of file
     */
    private static final int EOF = -1;

    public ZipUtils() {
        throw new UnsupportedOperationException("不可实例化ZipUtils");
    }

    // --------------------------------------
    // zlib
    // --------------------------------------
    /**
     * zlib压缩
     * @param file
     * @return
     * @throws IOException
     */
//    public static File zlib(File file) throws IOException {
//        
//        // 参数校验 //
//        Objects.requireNonNull(file, "参数file不能为null");
//        if(!file.exists()) {
//            throw new IOException(String.format("文件 不存在，文件路径：%s",file.getCanonicalPath()));
//        }
//        if(file.isDirectory()) {
//            throw new IOException(String.format("文件 不能是文件夹，文件路径：%s",file.getCanonicalPath()));
//        }
//        if(!file.canRead()) {
//            throw new IOException(String.format("文件不可读，文件路径：%s",file.getCanonicalPath()));
//        }
//        
//        // 压缩文件 //
//        String fileName = file.getName().substring(0,file.getName().lastIndexOf("."));
//        File compressFile = new File(file.getParent(), fileName + ZLIB_EXT);
//        try(FileInputStream in = new FileInputStream(file);
//                FileOutputStream fileOut = new FileOutputStream(compressFile);
//                DeflaterOutputStream out = new DeflaterOutputStream(fileOut)) {
//            copyIO(in,out);
//        } catch(IOException e) {
//            throw new IOException("文件压缩失败",e);
//        }
//        return compressFile;
//    }

    /**
     * zlib解压
     * @param file
     * @return
     * @throws IOException
     */
//    public static File unzlib(File file) throws IOException {
//        
//        // 参数校验 //
//        Objects.requireNonNull(file, "参数file不能为null");
//        if(!file.exists()) {
//            throw new IOException(String.format("文件 不存在，文件路径：%s",file.getCanonicalPath()));
//        }
//        if(file.isDirectory()) {
//            throw new IOException(String.format("文件 不能是文件夹，文件路径：%s",file.getCanonicalPath()));
//        }
//        if(!file.canRead()) {
//            throw new IOException(String.format("文件不可读，文件路径：%s",file.getCanonicalPath()));
//        }
//        
//        // 解压文件 //
//        String fileName = file.getName().substring(0,file.getName().lastIndexOf("."));
//        File decompressFile = new File(file.getParentFile(), fileName);
//        try(FileInputStream fileIn = new FileInputStream(file);
//                DeflaterInputStream in = new DeflaterInputStream(fileIn);
//                FileOutputStream out = new FileOutputStream(decompressFile)) {
//            copyIO(in,out);
//        } catch(IOException e) {
//            throw new IOException("文件解压失败",e);
//        }
//        return decompressFile;
//    }

    // --------------------------------------
    // gzip
    // --------------------------------------
    /**
     * gzip压缩文件
     * @param file
     * @return
     * @throws IOException
     */
    public static File gzip(File file) throws IOException {

        // 参数校验 //
        if (file.isDirectory()) {
            throw new IOException(String.format("文件不能是文件夹，文件路径：%s", file.getCanonicalPath()));
        }
        checkFile(file);

        // 压缩文件 //
        String fileName = file.getName().substring(0, file.getName().lastIndexOf(".")) + GZ_EXT;
        File compressFile = new File(file.getParent(), fileName);
        try (FileInputStream in = new FileInputStream(file);
                FileOutputStream fileOut = new FileOutputStream(compressFile);
                GZIPOutputStream out = new GZIPOutputStream(fileOut)) {
            copyIO(in, out);
        } catch (IOException e) {
            throw new IOException("压缩失败", e);
        }
        return compressFile;
    }

    /**
     * gzip解压文件
     * @param file
     * @return
     * @throws IOException
     */
    public static File ungzip(File file) throws IOException {

        // 参数校验 //
        if (file.isDirectory()) {
            throw new IOException(String.format("文件不能是文件夹，文件路径：%s", file.getCanonicalPath()));
        }
        checkFile(file);

        // 压缩文件 //
        String fileName = file.getName().substring(0, file.getName().lastIndexOf("."));
        File decompressFile = new File(file.getParent(), fileName);
        try (FileInputStream fileIn = new FileInputStream(file);
                GZIPInputStream in = new GZIPInputStream(fileIn);
                FileOutputStream out = new FileOutputStream(decompressFile)) {
            copyIO(in, out);
        } catch (IOException e) {
            throw new IOException("解压失败", e);
        }
        return decompressFile;
    }

    /**
     * 检测{@code file}对象的存在性与可读性
     * @param file
     * @throws IOException
     */
    private static void checkFile(File file) throws IOException {

        Objects.requireNonNull(file, "参数file不能为null");
        if (!file.exists()) {
            throw new IOException(String.format("文件 不存在，文件路径：%s", file.getCanonicalPath()));
        }
        if (!file.canRead()) {
            throw new IOException(String.format("文件不可读，文件路径：%s", file.getCanonicalPath()));
        }
    }

    // --------------------------------------
    // zip
    // --------------------------------------

    /**
     * zip压缩文件
     * @param file
     * @return
     * @throws IOException
     */
    public static File zip(File file) throws IOException {

        // 参数校验 //
        checkFile(file);

        // 压缩文件 //
        int pointIndex = file.getName().lastIndexOf(".");
        String fileName = null;
        if (pointIndex != -1) { // 有后缀名
            fileName = file.getName().substring(0, file.getName().lastIndexOf(".")) + ZIP_EXT;
        } else {
            fileName = file.getName() + ZIP_EXT;
        }
        File compressFile = new File(file.getParent(), fileName);
        try (FileOutputStream fileOut = new FileOutputStream(compressFile);
                CheckedOutputStream checkedOut = new CheckedOutputStream(fileOut, new CRC32());
                ZipOutputStream out = new ZipOutputStream(checkedOut);) {
            if (file.isFile()) {
                zipFile(out, file);
            } else {
                zipDir(out, file);
            }
        } catch (IOException e) {
            throw new IOException("压缩失败", e);
        }
        return compressFile;

    }

    private static void zipDir(ZipOutputStream out, File dir) throws IOException {

        File[] fileArr = dir.listFiles();
        if (fileArr != null) {
            for (File file : fileArr) {
                zipFile(out, file);
            }
        }

    }

    private static void zipFile(ZipOutputStream out, File file) throws IOException {

        try (FileInputStream in = new FileInputStream(file)) {
            out.putNextEntry(new ZipEntry(file.getName()));
            copyIO(in, out);
            out.flush();
        } catch (IOException e) {
            throw new IOException(String.format("文件压缩失败，文件路径：%s", file.getCanonicalPath()), e);
        }
    }

    /**
     * zip解压文件
     * @param file
     * @throws ZipException
     * @throws IOException
     */
    public static File unzip(File file) throws ZipException, IOException {

        int size = zipFileSize(file);
        boolean isDir = true; // 解压后是获取一个文件夹还是一个文件
        if (size == 0) {
            throw new IOException(String.format("压缩文件中不包含需要解压的文件", file.getCanonicalPath()));
        } else if (size == 1) { // 仅一个待解压项，解压后直接获取文件
            isDir = false;
        }
        String fileName = file.getName().substring(0, file.getName().lastIndexOf("."));
        File decompressFile = new File(file.getParent(), fileName);
        try (FileInputStream fileIn = new FileInputStream(file);
                CheckedInputStream checkedIn = new CheckedInputStream(fileIn, new CRC32());
                ZipInputStream in = new ZipInputStream(checkedIn)) {
            if (isDir) {
                unzipDir(in,decompressFile);
            } else {
                in.getNextEntry(); //must do this
                unzipFile(in,decompressFile);
            }
        } catch (IOException e) {
            throw new IOException("文件解压失败", e);
        }
        return decompressFile;
    }
    private static void unzipDir(ZipInputStream in,File file) throws IOException {
        
        file.mkdirs();
        ZipEntry zipEntry = null;
        while((zipEntry = in.getNextEntry()) != null) {
            File newFile = new File(file,zipEntry.getName());
            unzipFile(in,newFile);
        }
    }
    private static void unzipFile(ZipInputStream in, File file) throws IOException {

        try (FileOutputStream out = new FileOutputStream(file)) {
            copyIO(in, out);
        } catch (IOException e) {
            throw new IOException(String.format("文件解压失败，文件路径：%s", file.getCanonicalPath()), e);
        }
    }

    private static int zipFileSize(File file) throws ZipException, IOException {

        ZipFile zipFile = new ZipFile(file);
        int size = 0;
        Enumeration<? extends ZipEntry> enumeration = zipFile.entries();
        while (enumeration.hasMoreElements()) {
            enumeration.nextElement();
            size++;
        }
        return size;
    }

    /**
     * 将输入流中的数据拷贝至输出流
     * @param in
     * @param out
     * @throws IOException
     */
    private static void copyIO(InputStream in, OutputStream out) throws IOException {

        byte[] buff = new byte[BUFFER_LEN];
        int len = 0;
        while ((len = in.read(buff)) != EOF) {
            out.write(buff, 0, len);
        }
    }
}
