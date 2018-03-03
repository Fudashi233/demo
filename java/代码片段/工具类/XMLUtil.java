package cn.edu.jxau.utils;

import java.io.File;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

public class XMLUtil {
	
	public static Document parse(String dir) throws DocumentException {
		
		SAXReader reader = new SAXReader();
		return reader.read(new File(dir));
	}
}
