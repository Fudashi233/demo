package DAO;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/** 
 * @author 付大石
 *
 *	data access object interface
 */
public interface DAOI {

	public int delete(String name,Map<String,Object> paramMap) throws SQLException;
	
	public int update(String name,Map<String,Object> paramMap) throws SQLException;
	
	public int insert(String name,Map<String,Object> paramMap) throws SQLException;
	
	public List<Map<String,Object>> select(String name,Map<String,Object> paramMap) throws SQLException;
}
