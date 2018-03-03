import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;




public class DOM4J
{
    public static void main(String[] args)
    {
//    	try
//    	{
//    		parse("books.xml");
//    	}
//    	catch(DocumentException ex)
//    	{
//    		ex.printStackTrace();
//    	}
    	createXML("haha.xml");
    }
    private static void parse(String dir) throws DocumentException
    {
    	SAXReader reader = new SAXReader();
    	Document document = reader.read(new File(dir));
    	Element root = document.getRootElement();
    	Iterator<Element> iter = root.elementIterator();
    	while(iter.hasNext())
    	{
    		Element e = iter.next();
    		List<Attribute> attr = e.attributes();
    		for(int i=0;i<attr.size();i++)
    		{
    			System.out.println(attr.get(i).getName()+"---"+attr.get(i).getValue());
    		}
    		Iterator<Element> children = e.elementIterator();
    		while(children.hasNext())
    		{
    			Element child = children.next();
    			System.out.println(child.getName()+"---"+child.getStringValue());
    		}
    		System.out.println("=======");
    	}
    }
    private static void createXML(String dir)
    {
    	Document document = DocumentHelper.createDocument();
    	
    	Element css = document.addElement("css");
    	css.addAttribute("verion","2.0");
    	Element channel = css.addElement("channel");
    	Element title = channel.addElement("title");
    	title.setText("南方周刊");
    	OutputFormat format = OutputFormat.createPrettyPrint();
    	XMLWriter writer = null;
		FileOutputStream output = null;
    	try 
    	{
			output = new FileOutputStream(dir);
			
			writer = new XMLWriter(output,format);
			//设置是否转义，默认值是true，代表转义
			//writer.setEscapeText(false);
			writer.write(document);
		}
    	catch (IOException e)
    	{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(writer!=null)
				{
					writer.close();
				    writer = null;
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
					if(output!=null)				
					{
						output.close();
						output = null;
					}
				}
				catch(IOException ex)
				{
					ex.printStackTrace();
				}
				finally
				{
					//nothing
				}
			}
		}
    }
}

