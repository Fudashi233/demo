package cn.edu.jxau;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Connection;

public class JDBCUtil
{
	private static String databaseName = "test";
	private static String url = "jdbc:mysql://localhost:3306/"+databaseName;	                            
	private static String className = "com.mysql.jdbc.Driver";
	public static Connection getConnection(String user,String password) throws SQLException
	{
		return DriverManager.getConnection(url,user,password);
	}
	public static void free(Connection c,Statement s,ResultSet r) throws SQLException
	{
		try
		{
			if(r!=null)
				r.close();
		}
		finally
		{
			try
			{
				if(s!=null)
					s.close();
			}
			finally
			{
				try
				{
					if(c!=null)
						c.close();
				}
				finally
				{
					//System.out.println("Clear over");
				}
			}
		}
	}
	static
	{
		try
		{
			Class.forName(className);	
		}
		catch(ClassNotFoundException ex)
		{
			System.out.println(ex);
		}
	}
}