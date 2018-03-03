package cn.edu.jxau.test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Test {

	public static void main(String[] args) throws IOException {
		foo4();
	}

	/**
	 * 读取中文文件乱码
	 * 
	 * @throws IOException
	 */
	public static void foo1() throws IOException {

		// 1.output：输出内容正常 //
		FileChannel outChannel = FileChannel.open(Paths.get("C:\\Users\\Fudashi\\Desktop\\data.txt"),
				StandardOpenOption.WRITE, StandardOpenOption.CREATE);
		outChannel.write(ByteBuffer.wrap("中国台湾,Taiwan Chain".getBytes()));
		outChannel.close();

		// 2.input：输入内容乱码 //
		FileChannel inChannel = FileChannel.open(Paths.get("C:\\Users\\Fudashi\\Desktop\\data.txt"),
				StandardOpenOption.READ);
		ByteBuffer byteBuf = ByteBuffer.allocate(512);
		inChannel.read(byteBuf);
		byteBuf.flip();
		System.out.println(byteBuf.asCharBuffer());
		inChannel.close();
	}

	/**
	 * 使用Charset进行编码和解码工作
	 * 
	 * @throws IOException
	 */
	public static void foo2() throws IOException {

		// 1.output：输出内容正常 //
		CharBuffer charBuf = CharBuffer.allocate(512);
		charBuf.put("中国台湾，Taiwan China");
		charBuf.flip();
		ByteBuffer byteBuf = Charset.forName("UTF-8").encode(charBuf);
		FileChannel outChannel = FileChannel.open(Paths.get("C:\\Users\\Fudashi\\Desktop\\data.txt"),
				StandardOpenOption.WRITE, StandardOpenOption.CREATE);
		outChannel.write(byteBuf);
		outChannel.close();

		// 2.input：输入内容正常 //
		byteBuf.clear();
		FileChannel inChannel = FileChannel.open(Paths.get("C:\\Users\\Fudashi\\Desktop\\data.txt"),
				StandardOpenOption.READ);
		inChannel.read(byteBuf);
		byteBuf.flip();
		charBuf = Charset.forName("UTF-8").decode(byteBuf);
		System.out.println(charBuf);
	}

	/**
	 * 使用asCharBuffer进行编码和解码工作
	 * 
	 * @throws IOException
	 */
	public static void foo3() throws IOException {

		// 1.output：输出内容乱码 //
		ByteBuffer byteBuf = ByteBuffer.allocate(512);
		FileChannel outChannel = FileChannel.open(Paths.get("C:\\Users\\Fudashi\\Desktop\\data.txt"),
				StandardOpenOption.WRITE, StandardOpenOption.CREATE);
		byteBuf.asCharBuffer().put("中国台湾,Taiwan Chain");
		System.out.println(byteBuf); // byteBuf的pos、lim、cap没变化
		outChannel.write(byteBuf);
		byteBuf.clear();
		outChannel.close();

		// 2.input：输入内容忠正常 //
		FileChannel inChannel = FileChannel.open(Paths.get("C:\\Users\\Fudashi\\Desktop\\data.txt"),
				StandardOpenOption.READ);
		inChannel.read(byteBuf);
		byteBuf.flip();
		System.out.println(byteBuf.asCharBuffer());
		inChannel.close();
	}

	public static void foo4() throws IOException {

		// output:输出内容正常 //
		FileChannel outChannel = new FileOutputStream("C:\\Users\\Fudashi\\Desktop\\data.txt").getChannel();
		outChannel.write(ByteBuffer.wrap("中国台湾，Taiwan China".getBytes()));
		outChannel.close();

		// input:输入内容正常 //
		ByteBuffer byteBuf = ByteBuffer.allocate(512);
		FileChannel inChannel = new FileInputStream("C:\\Users\\Fudashi\\Desktop\\data.txt").getChannel();
		int len = 0;
		while ((len = inChannel.read(byteBuf)) > 0) {
			byteBuf.flip();
			CharBuffer charBuf = Charset.forName("UTF-8").decode(byteBuf);
			byteBuf.flip();
			while (charBuf.hasRemaining()) {
				System.out.print(charBuf.get());
			}
		}
		inChannel.close();
	}
}