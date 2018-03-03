package DAO.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentException;

import DAO.DAOI;
import utils.DAOUtil;

public class DAOImpl implements DAOI{

	@Override
	public int delete(String name,Map<String,Object> paramMap) throws SQLException {
		
		PreparedStatement statement = null;
		try {
			
			statement = (PreparedStatement) DAOUtil.getSQL(name,paramMap);
		} catch (DocumentException ex) {
			
			ex.printStackTrace();
		}
		
		return statement.executeUpdate();
	}
	
	@Override
	public int update(String name,Map<String,Object> paramMap) throws SQLException {
		
		PreparedStatement statement = null;
		try {
			
			statement = (PreparedStatement) DAOUtil.getSQL(name,paramMap);
		} catch (DocumentException ex) {
			
			ex.printStackTrace();
		}	
		return statement.executeUpdate();
	}
	
	@Override
	public int insert(String name,Map<String,Object> paramMap) throws SQLException {
	
		PreparedStatement statement = null;
		try {
			
			statement = (PreparedStatement) DAOUtil.getSQL(name,paramMap);
		} catch (DocumentException ex) {
			
			ex.printStackTrace();
		}
		if(statement!=null) {
			
			return statement.executeUpdate();
		}
		return 0;
	}
	
	@Override
	public List<Map<String,Object>> select(String name,Map<String,Object> paramMap) throws SQLException{
		
		PreparedStatement statement = null;
		try {
			
			statement = (PreparedStatement) DAOUtil.getSQL(name,paramMap);
		} catch(DocumentException ex) {
			
			ex.printStackTrace();
		}
		if(statement!=null) {
			
			ResultSet resultSet = statement.executeQuery();
			List<Map<String,Object>> list = new LinkedList<Map<String,Object>>();
			while(resultSet.next()) {
				
				Map<String,Object> map = new HashMap<String,Object>();
				ResultSetMetaData metaData = resultSet.getMetaData();
				for(int i=0;i<metaData.getColumnCount();i++) {
					
					map.put(metaData.getColumnName(i+1),resultSet.getObject(i+1));
				}
				list.add(map);
			}
			return list;
		}
		return null;
	}
}
