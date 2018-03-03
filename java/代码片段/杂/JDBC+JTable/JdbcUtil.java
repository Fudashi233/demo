package cn.edu.jxau;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;

public class JdbcUtil
{
    private static String url = "jdbc:mysql://localhost:3306/test";
    private static String user = "root";
    private static String password = "4869";
    private JdbcUtil()
    {
    	
    }
    public static Connection getConnection() throws SQLException
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
    				
    			}
			}
    	}
    }
    static
    {
    	try
    	{
    		Class.forName("com.mysql.jdbc.Driver");
    	}
    	catch(ClassNotFoundException ex)
    	{
    		System.out.println("58---"+ex);
    	}
    }
}
