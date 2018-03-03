import org.jdom2.Attribute;
import org.jdom2.CDATA;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;

import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

public class JDOM {
	public static void main(String[] args)
	{
//		try 
//		{
//			parse("books.xml");
//		} 
//		catch (IOException | JDOMException e)
//		{
//			e.printStackTrace();
//		}
		createXML("haha.xml");
	}

	private static void parse(String dir) throws IOException, JDOMException
	{
		SAXBuilder xBuilder = new SAXBuilder();
		FileInputStream input = new FileInputStream(dir);
		
		Document document = xBuilder.build(input);
		Element root = document.getRootElement();
		List<Element> list = root.getChildren();
		for (int i = 0; i < list.size(); i++) 
		{
			 List<Attribute> attributeList = list.get(i).getAttributes();
			 for(int j=0;j<attributeList.size();j++)
			 {
			     System.out.println(attributeList.get(j).getName()+"---"+attributeList.get(j).getValue());
			 }
			//System.out.println(list.get(i).getAttributeValue("id"));   //已知属性时可以该方法
			 List<Element> nodeList = list.get(i).getChildren();//获取所有的孩子节点
			 for(int k=0;k<nodeList.size();k++)
			 {
				 System.out.println(nodeList.get(k).getName()+"---"+nodeList.get(k).getValue());  
			 }
			 System.out.println("===========");
		}
	}

	private static void createXML(String dir)
	{
		Element rss = new Element("css");
		rss.setAttribute("version", "2.0");
		Document document = new Document(rss);
		
		Element channel = new Element("channel");
		rss.addContent(channel);
		
		Element title = new Element("title");
		CDATA cdata = new CDATA("上海移动互联网产业促进中心正式揭牌");
		title.setContent(cdata);
		channel.addContent(title);
		
		Format format = Format.getPrettyFormat();
		format.setEncoding("GBK");
		XMLOutputter output = new XMLOutputter(format);





		try
		{
			output.output(document,new FileOutputStream(dir));
		}
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
