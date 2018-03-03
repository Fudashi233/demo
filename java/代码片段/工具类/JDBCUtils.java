package cn.edu.jxau.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;


/* driver=com.mysql.jdbc.Driver
URL=jdbc:mysql://localhost:3306/test
username=root
password=root */

public class JDBCUtils {

    private static final String DRIVER;
    private static final String URL;
    private static final String USERNAME;
    private static final String PASSWORD;

    /**
     * 静态代码块，在JDBCUtils初始化之前根据JDBC.properties配置文件
     * 获取{@field driver}、{@field URL}、{@field username}
     * {@field password}四个数据域的值
     */
    static {

        // 加载配置文件 //
        Properties prop = new Properties();
        try (InputStream input = JDBCUtils.class.getClassLoader().getResourceAsStream("JDBC.properties");) {
            prop.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        DRIVER = prop.getProperty("driver");
        URL = prop.getProperty("URL");
        USERNAME = prop.getProperty("username");
        PASSWORD = prop.getProperty("password");

        // 加载驱动 //
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    private JDBCUtils() {
        throw new UnsupportedOperationException("请勿实例化JDBCUtils");
    }
    
    /**
     * 获取数据库连接
     * @return
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {

        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new SQLException("连接获取失败", e);
        }
    }
    
    /**
     * 关闭数据库连接
     * @param connection
     * @param statement
     * @param resultSet
     * @throws SQLException
     */
    public static void close(Connection connection, Statement statement, ResultSet resultSet) throws SQLException {

        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException e) {
            throw new SQLException("ResultSet关闭失败", e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                throw new SQLException("Statement关闭失败", e);
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    throw new SQLException("Connection关闭失败", e);
                }
            }
        }
    }

}
