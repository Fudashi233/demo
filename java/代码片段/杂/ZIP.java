import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.zip.Adler32;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.CheckedOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Test
{
	public static void main(String[] args) throws IOException   
    {   
		BufferedReader[] readers = {new BufferedReader(new FileReader("sample.txt")),new BufferedReader(new FileReader("sample.txt"))};
		ZipOutputStream zos = new ZipOutputStream(new CheckedOutputStream(new FileOutputStream("x.zip"),new Adler32()));
		BufferedOutputStream output = new BufferedOutputStream(zos);
		int temp = 0;
		zos.setComment("qweasdzxc");
		for(int i=0;i<readers.length;i++)
		{
			zos.putNextEntry(new ZipEntry(""+i));
			
			while((temp = readers[i].read())>=0)
			{
				output.write(temp);
			}
			output.flush();
		}
		output.close();
		//------------------------------
		ZipInputStream zis = new ZipInputStream(new CheckedInputStream(new FileInputStream("x.zip"),new CRC32()));
		ZipEntry ze = null;
		BufferedInputStream input = new BufferedInputStream(zis);
		while((ze = zis.getNextEntry())!=null)
		{
			System.out.println("51	"+ze.getComment());
			System.out.println("51	"+ze.getTime());
			System.out.println("52	"+ze);
			while((temp = input.read())>=0)
			{
				System.out.print((char)temp);
			}
			
		}
    }
}