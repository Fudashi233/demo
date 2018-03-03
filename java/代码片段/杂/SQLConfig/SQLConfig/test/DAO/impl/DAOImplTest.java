package DAO.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;

public class DAOImplTest {

	@Test
	public void testDelete() {
		
		//构造测试数据
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("idX","3");
		DAOImpl daoImpl = new DAOImpl();
		
		//开始测试
		try {
			
			System.out.println(daoImpl.delete("deleteX",paramMap));
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
	
	@Test
	public void testInsert() {
		
		//构造测试数据
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("id","5");
		paramMap.put("date",new java.sql.Date(System.currentTimeMillis()));
		paramMap.put("idx","3");
		DAOImpl daoImpl = new DAOImpl();
		
		//开始测试
		try {
			
			System.out.println(daoImpl.insert("insertX",paramMap));
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
	
	@Test
	public void testUpdate() {
		
		//构造测试数据
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("destTitle","CAR");
		paramMap.put("srcTitle","car");
		DAOImpl daoImpl = new DAOImpl();
		
		//开始测试
		try {
			
			System.out.println(daoImpl.update("updateX",paramMap));
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}

	@Test
	public void testSelect() {
		
		//构造测试数据
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("courseID",7);
		paramMap.put("credits",2);
		DAOImpl daoImpl = new DAOImpl();
		
		//开始测试
		try {
			
			List<Map<String,Object>> list =  daoImpl.select("selectCourse",paramMap);
			for(int i=0,size=list.size();i<size;i++) {
				
				Map<String,Object> map = list.get(i);
				printMap(map);
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
	
	private void printMap(Map<String,Object> map) {
		
		Set<Entry<String,Object>> set = map.entrySet();
		Iterator<Entry<String,Object>> iterator = set.iterator();
		while(iterator.hasNext()) {
			
			Entry<String,Object> entry = iterator.next();
			System.out.print(entry.getKey()+" "+entry.getValue()+"	");
		}
		System.out.println("\n------");
	}
}
