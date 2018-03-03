package utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.junit.Test;

public class XMLUtilTest {
	
	private Document document;
	public XMLUtilTest() {
		
		try {
			
			document = XMLUtil.parse("SQLConfig.xml");
		} catch (DocumentException e) {

			e.printStackTrace();
		}
	}
	@Test
	public void testGetElementByName() {
		
		Element operation = XMLUtil.getElementByName(document,"deleteUser");
		
	}

}
