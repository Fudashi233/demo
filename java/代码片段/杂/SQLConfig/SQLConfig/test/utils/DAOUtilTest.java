package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.junit.Test;

import bean.Condition;
import bean.Join;
import bean.Order;
import bean.Table;

public class DAOUtilTest {

	@Test
	public void test() throws FileNotFoundException,IOException {
		
		File f = new File("asd");
		FileInputStream in = new FileInputStream(f);
	}
	
	@Test
	public void testElement2Tables() {

		Document document = null;
		try {
			
			document = XMLUtil.parse("SQLConfig.xml");
		} catch (DocumentException e) {

			e.printStackTrace();
		}
		Element element = XMLUtil.getElementByName(document,"selectUser");
		printTable(DAOUtil.element2Tables(element.element("tables")));
	}
	
	@Test
	public void testElement2Conditions() {
		
		Document document = null;
		try {
			
			document = XMLUtil.parse("SQLConfig.xml");
		} catch (DocumentException e) {

			e.printStackTrace();
		}
		Element element = XMLUtil.getElementByName(document,"deleteUser");
		printCondition(DAOUtil.element2Conditions(element.element("conditions")));
	} 
	
	@Test
	public void testElement2Joins() {
		
		Document document = null;
		try {
			
			document = XMLUtil.parse("SQLConfig.xml");
		} catch (DocumentException e) {

			e.printStackTrace();
		}
		Element element = XMLUtil.getElementByName(document,"selectUser");
		printJoin(DAOUtil.element2Joins(element.element("joins")));
	}
	
	@Test
	public void testElement2Orders() {
		
		Document document = null;
		try {
			
			document = XMLUtil.parse("SQLConfig.xml");
		} catch (DocumentException e) {

			e.printStackTrace();
		}
		Element element = XMLUtil.getElementByName(document,"selectUserX");
		printOrder(DAOUtil.element2Orders(element.element("orders")));
	}
	
	@Test
	public void testGetSQL() {
		
		//开始测试
		try {
			
			DAOUtil.getSQL("selectCourse",new HashMap());
		} catch (DocumentException e) {
			
			e.printStackTrace();
		} catch (SQLException e) {

			e.printStackTrace();
		}
		
	}
	private void printTable(Table table) {
		
		System.out.println("===============");
		System.out.println(table.getTableName());
		System.out.println(Arrays.toString(table.getColumns()));
		System.out.println(table.getKey());
		System.out.println("===============");
	}
	private void printTable(Table[] tableArr) {
		
		for(int i=0;i<tableArr.length;i++) {
			
			printTable(tableArr[i]);
		}
	}
	private void printCondition(Condition condition) {
		
		System.out.println("=============");
		System.out.println(condition);
		System.out.println("=============");
	}
	private void printCondition(Condition[] conditionArr) {
		
		for(int i=0;i<conditionArr.length;i++) {
			
			printCondition(conditionArr[i]);
		}
	}
	
	private void printJoin(Join join) {
		
		System.out.println("--------------");
		System.out.println(join);
		System.out.println("--------------");
	}
	
	private void printJoin(Join[] joinArr) {
		
		for(int i=0;i<joinArr.length;i++) {
			
			printJoin(joinArr[i]);
		}
	}
	
	private void printOrder(Order order) {
		
		System.out.println("--------------");
		System.out.println(order);
		System.out.println("--------------");
	}
	private void printOrder(Order[] orderArr) {
		
		for(int i=0;i<orderArr.length;i++) {
			
			printOrder(orderArr[i]);
		}
	}
}
