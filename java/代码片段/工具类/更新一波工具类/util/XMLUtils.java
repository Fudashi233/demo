package cn.edu.jxau.util;

import java.io.File;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

public class XMLUtils {

    public static Document parse(String dir) throws DocumentException {

        SAXReader reader = new SAXReader();
        return reader.read(new File(dir));
    }

    public static Document parse(File file) throws DocumentException {

        SAXReader reader = new SAXReader();
        return reader.read(file);
    }
}
