import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class SAX
{
    public static void main(String[] args)
    {
//    	try 
//    	{
//			parse("books.xml",new MySAXHandler());
//		} 
//    	catch (SAXException | ParserConfigurationException | IOException e) 
//    	{
//			e.printStackTrace();
//		}
    	ArrayList<Book> list = new ArrayList<Book>();
    	list.add(new Book("1","冰与火之歌","乔治马丁","2014","88",null));
    	list.add(new Book("2","安徒生童话",null,"2014","77",null));
    	try
    	{
			createXML(list,"haha.xml");
		}
    	catch (TransformerConfigurationException | FileNotFoundException | SAXException e)
    	{
			e.printStackTrace();
		}
    	
    }
    private static void parse(String dir,DefaultHandler handler) throws SAXException, ParserConfigurationException, IOException
    {
    	SAXParserFactory factory = SAXParserFactory.newInstance();
    	SAXParser parser = factory.newSAXParser();
    	parser.parse(dir,handler);
    }
    private static void createXML(ArrayList<Book> list,String dir) throws TransformerConfigurationException, FileNotFoundException, SAXException
    {
    	SAXTransformerFactory tf = (SAXTransformerFactory)SAXTransformerFactory.newInstance();
    	TransformerHandler handler = tf.newTransformerHandler();
    	
    	Transformer t = handler.getTransformer();
    	t.setOutputProperty(OutputKeys.INDENT,"yes");
    	t.setOutputProperty(OutputKeys.ENCODING,"UTF-8");
    	
    	Result r = new StreamResult(new FileOutputStream(dir));
    	AttributesImpl attr = new AttributesImpl();
    	handler.setResult(r);
    	handler.startDocument();
        handler.startElement(null,null,"bookstore",null);
        for(Book book:list)
        {
        	attr.addAttribute(null,null,"id",null,book.id);
        	handler.startElement(null,null,"book",attr);
        	attr.clear();
        	System.out.println(book.name.trim().equals(""));
        	if(book.name!=null&&!book.name.trim().equals(""))
        	{
        		handler.startElement(null,null,"name",null);
        		handler.characters(book.name.toCharArray(),0,book.name.length());
        		handler.endElement(null,null,"name");
        	}
        	if(book.author!=null&&!book.author.trim().equals(""))
        	{
        		handler.startElement(null,null,"author",null);
        		handler.characters(book.author.toCharArray(),0,book.author.length());
        		handler.endElement(null,null,"author");
        	}
        	if(book.year!=null&&!book.year.trim().equals(""))
        	{
        		handler.startElement(null,null,"year",null);
        		handler.characters(book.year.toCharArray(),0,book.year.length());
        		handler.endElement(null,null,"year");
        	}
        	if(book.price!=null&&!book.price.trim().equals(""))
        	{
        		handler.startElement(null,null,"price",null);
        		handler.characters(book.price.toCharArray(),0,book.price.length());
        		handler.endElement(null,null,"price");
        	}
        	if(book.language!=null&&!book.language.trim().equals(""))
        	{
        		handler.startElement(null,null,"language",null);
        		handler.characters(book.language.toCharArray(),0,book.language.length());
        		handler.endElement(null,null,"language");
        	}
        	handler.endElement(null,null,"book");
        	
        }
        handler.endElement(null,null,"bookstore");
    	handler.endDocument();
    }
}
class MySAXHandler extends DefaultHandler
{
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException 
	{
		super.startElement(uri, localName, qName, attributes);  
		if(qName.equals("book"))
		{
//			System.out.println(attributes.getValue("id"));   //知道属性名
			for(int i=0;i<attributes.getLength();i++)
			{
				System.out.print(attributes.getQName(i)+"---");
				System.out.println(attributes.getValue(i));
			}
		}
		else if(qName.equals("bookstore"))
		{
			//nothing
		}
		else
		{
			System.out.print(qName+"---");
		}		
	}
	@Override
	public void endElement(String uri,String localName,String qName) throws SAXException
	{
		super.endElement(uri,localName,qName);
		if(qName.equals("book"))
		    System.out.println("=========");
	}
	@Override
	public void characters(char[] ch,int start,int length)
	{
		String element = new String(ch,start,length);
		if(!element.trim().equals(""))
		    System.out.println(element);
	}
}
class Book 
{
	public String id;
	public String name;
	public String author;
	public String year;
	public String price;
	public String language;
	public Book(String id,String name,String author,String year,String price,String language)
	{
		this.id = id;
		this.name = name;
		this.author = author;
		this.year = year;
		this.price = price;
		this.language = language;
	}
}