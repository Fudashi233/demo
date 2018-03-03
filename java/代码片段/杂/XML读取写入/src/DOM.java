import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import org.xml.sax.SAXException;
public class DOM
{
	public static void main(String[] args)
	{
		createXML("haha.xml");
	}
	private static Document parse(String dir) throws ParserConfigurationException, SAXException, IOException
	{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		return db.parse(dir);
	}
	private static void printXML(Document document,String nodeName)
	{
		NodeList nodeList = document.getElementsByTagName(nodeName);
		for(int i=0;i<nodeList.getLength();i++)
		{
			NamedNodeMap nodeMap = nodeList.item(i).getAttributes();
			for(int j=0;j<nodeMap.getLength();j++)
			{
				Node node = nodeMap.item(j);
				System.out.print(node.getNodeName()+"---");
				System.out.println(node.getNodeValue());
			}
			System.out.println(nodeList.item(i).getNodeName()+"---"+nodeList.item(i).getNodeValue());
			NodeList children = nodeList.item(i).getChildNodes();
			for(int j=0;j<children.getLength();j++)
			{
				Node node = children.item(j);
				if(node.getNodeType()==Node.ELEMENT_NODE)
				{
					//System.out.println(node.getNodeName()+"---"+node.getTextContent());
					System.out.println(node.getNodeName()+"---"+node.getFirstChild().getNodeValue());
				}	
			}
			System.out.println("----------------");
		}
	}
	private static void printXML(Document document,String nodeName,String attrName)
	{
		NodeList nodeList = document.getElementsByTagName(nodeName);
		for(int i=0;i<nodeList.getLength();i++)
		{
			Element e = (Element)nodeList.item(i);
			NodeList list = e.getElementsByTagName("name");
			for(int j=0;j<list.getLength();j++)
			{
				System.out.println(list.item(j).getNodeName()+"---"+list.item(j).getNodeValue());
			}
		}
	}
	//===========================生成一个xml=============================
	private static void createXML(String dir)
	{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try 
		{
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.newDocument();
			document.setXmlStandalone(true);
			
			Element bookstore = document.createElement("bookstore");
			document.appendChild(bookstore);
			
			Element book1 = document.createElement("book");
			book1.setAttribute("id", "1");
			Element book2 = document.createElement("book");
			book2.setAttribute("id","2");
			bookstore.appendChild(book1);
			bookstore.appendChild(book2);
			
			Element name = document.createElement("name");
			name.setTextContent("冰与火之歌");
			Element author = document.createElement("author");
			author.setTextContent("乔治马丁");
			Element year = document.createElement("year");
			year.setTextContent("2014");
			Element price = document.createElement("price");
			price.setTextContent("89");
			book1.appendChild(name);
			book1.appendChild(author);
			book1.appendChild(year);
			book1.appendChild(price);
			
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer t = tf.newTransformer();
			t.setOutputProperty(OutputKeys.INDENT,"yes");
			t.transform(new DOMSource(document),new StreamResult(new File(dir)));			    
		}
		catch (ParserConfigurationException|TransformerException e)
		{
			e.printStackTrace();
		}
	}
}