package utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/** 
 * @author 付大石
 *
 * java database util
 */
public class JDBCUtil {

	private static final String USER;
	private static final String PASSWORD;
	private static final String URL;
	private static final String DRIVER;
	static {
		
		InputStream in = JDBCUtil.class.getResourceAsStream("/JDBC.properties");
		Properties properties = new Properties();
		try {
			
			properties.load(in);
		} catch (IOException e) {

			e.printStackTrace();
		}
		USER = properties.getProperty("user");
		PASSWORD = properties.getProperty("password");
		URL = properties.getProperty("URL");
		DRIVER = properties.getProperty("driver");
		//加载驱动
		try {
			
			Class.forName(DRIVER);
		} catch(ClassNotFoundException ex) {
			
			ex.printStackTrace();
		}
	}
	public static Connection getConnection() throws SQLException {
		
		return DriverManager.getConnection(URL,USER,PASSWORD);
	}
	
	public static void close(Connection c,Statement s,ResultSet r) {
		
		try {
			
			r.close();
		} catch(SQLException ex) {
			
			
		} finally {
			
			try {
				
				s.close();
			} catch(SQLException ex) {
				
				ex.printStackTrace();
			} finally {
				
				try {
					
					c.close();
				} catch(SQLException ex) {
					
					ex.printStackTrace();
				}
			}
		}
	}
	
	public static void commit(Connection c) throws SQLException {
		
		c.commit();
	}
	
	public static void rollback(Connection c) throws SQLException {
			
		c.rollback();
	}
}
