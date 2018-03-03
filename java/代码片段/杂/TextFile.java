import java.util.ArrayList;
import java.util.Arrays;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.FileNotFoundException;

public class TextFile extends ArrayList<String>
{
	public static void main(String[] args)
	{
		TextFile t = new TextFile("sample.txt","");
		for(int i=0,size = t.size();i<size;i++)
		{
			System.out.print(t.get(i)+" ");
		}
	}
	public TextFile(String filename,String regex)
	{
		super(Arrays.asList(read(filename).split(regex)));
	}
	public static String read(String filename)
	{
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = null;
		try
		{
			reader = new BufferedReader(new FileReader(filename));
			String temp = null;
			while((temp = reader.readLine())!=null)
			{
				sb.append(temp);
				sb.append("\n");
			}
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			try 
			{
				if(reader!=null)
						reader.close();			
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	public void write(String filename,String text) throws FileNotFoundException
	{
		PrintWriter writer = null;
		try
		{
			writer = new PrintWriter(new File(filename));
			writer.print(text);
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			if(writer!=null)
				writer.close();
		}
	}
}
