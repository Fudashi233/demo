package cn.edu.jxau.test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Test {

	public static void main(String[] args) throws IOException {

		copy3();
	}

	public static void copy1() throws IOException {

		// 1.获取通道 //
		try (FileInputStream fileIn = new FileInputStream("C:\\Users\\Fudashi\\Desktop\\data.txt");
				FileChannel in = fileIn.getChannel();
				FileOutputStream fileOut = new FileOutputStream("C:\\Users\\Fudashi\\Desktop\\dataX.txt");
				FileChannel out = fileOut.getChannel();) {

			// 2.分配缓冲区 //
			ByteBuffer buf = ByteBuffer.allocate(1024);

			// 3.复制 //
			while (in.read(buf) != -1) {
				buf.flip();
				out.write(buf);
				buf.clear();
			}
		} catch (IOException e) {
			throw new IOException("文件复制失败", e);
		}
	}

	public static void copy2() throws IOException {

		// 1.获取通道 //
		try (FileChannel in = FileChannel.open(Paths.get("C:\\Users\\Fudashi\\Desktop\\data.txt"),
				StandardOpenOption.READ);
				FileChannel out = FileChannel.open(Paths.get("C:\\Users\\Fudashi\\Desktop\\dataX.txt"),
						StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE)) {

			// 2.获取内存映射 //
			MappedByteBuffer inBuffer = in.map(MapMode.READ_ONLY, 0, in.size());
			MappedByteBuffer outBuffer = out.map(MapMode.READ_WRITE, 0, in.size());

			// 3.复制文件 //
			byte[] temp = new byte[inBuffer.limit()];
			inBuffer.get(temp);
			outBuffer.put(temp);
		} catch (IOException e) {
			throw new IOException("文件复制失败", e);
		}
	}
	
	public static void copy3() throws IOException {
		
		// 1.获取通道 //
		try (FileChannel in = FileChannel.open(Paths.get("C:\\Users\\Fudashi\\Desktop\\data.txt"),
				StandardOpenOption.READ);
				FileChannel out = FileChannel.open(Paths.get("C:\\Users\\Fudashi\\Desktop\\dataX.txt"),
						StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE)) {
			
			System.out.println(in.size()+"	"+out.size());
			// 2.复制文件 //
			in.transferTo(0,in.size(),out);
		} catch (IOException e) {
			throw new IOException("文件复制失败", e);
		}
	}
}