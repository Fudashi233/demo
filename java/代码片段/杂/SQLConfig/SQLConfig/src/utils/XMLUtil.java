package utils;

import java.io.File;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class XMLUtil {

	public static Document parse(String dir) throws DocumentException {
		
		SAXReader reader = new SAXReader();
		return reader.read(new File(dir));
	}
	
	public static Element getElementByName(Document document,String name) {
		
		if(document==null) 
			return null;
		if(name==null || "".equals(name.trim()))
			return null;
		Element root = document.getRootElement();
		Iterator<Element> iterator = root.elementIterator();
		while(iterator.hasNext()) {
			
			Element element = iterator.next();
			if(name.equals(element.attribute("name").getValue())) {
				
				return element;
			}
		}
		return null;
	}
}
