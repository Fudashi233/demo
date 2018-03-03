package cn.edu.jxau;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class JDBCUtil
{
	private static String url = "jdbc:mysql://localhost:3306/";
	private static String className = "com.mysql.jdbc.Driver";
	public static Connection getConnection(String databaseName,String user,String password) throws SQLException
	{
		return DriverManager.getConnection(url+databaseName,user,password);
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