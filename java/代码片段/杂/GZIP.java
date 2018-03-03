import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Test
{
	public static void main(String[] args) throws IOException   
    {   
		BufferedReader bufferedReader = new BufferedReader(new FileReader("sample.txt"));
		BufferedOutputStream out = new BufferedOutputStream(new GZIPOutputStream(new FileOutputStream("sample.gz")));
		int temp = 0;
		while((temp = bufferedReader.read())>=0)
		{
			out.write(temp);
		}
		out.close();
		bufferedReader.close();
		
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("sampleX.txt"));
		BufferedInputStream in = new BufferedInputStream(new GZIPInputStream(new FileInputStream("sample.gz")));
		while((temp = in.read())>=0)
		{
			bufferedWriter.write(temp);
		}
		in.close();
		bufferedWriter.close();	
    }
}