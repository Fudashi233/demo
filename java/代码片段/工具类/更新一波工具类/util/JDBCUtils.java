package cn.edu.jxau.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

/**
 * 
 * @author 付大石
 */
public class JDBCUtils {

    private static final String DRIVER;
    private static final String URL;
    private static final String USERNAME;
    private static final String PASSWORD;
    private static SqlsMap sqlsMap;
    
    /**
     * 静态代码块，在JDBCUtils初始化之前根据JDBC.properties配置文件
     * 设置{@field driver}、{@field URL}、{@field username}
     * {@field password}四个数据域的值
     */
    static {
        // 加载sqls配置文件 //
        sqlsMap = new SqlsMap(ConfigManager.getConfig("sqls"));
        
        // 加载JDBC配置文件 //
        Properties prop = new Properties();
        try (InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(ConfigManager.getConfig("JDBC"));) {
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
    
    public static String getSql(String key) {
        return sqlsMap.get(key);
    }
    
    private static class SqlsMap {
        
        private Map<String, String> map;
        
        public SqlsMap(String path) {
            map = new HashMap<String, String>();
            try {
                loadConfig(path);
            } catch (DocumentException e) {
                throw new RuntimeException("配置文件加载失败",e);
            }
        }
        
        /**
         * 加载sqls配置文件
         * @param path sqls配置文件所在路径
         * @throws DocumentException
         */
        private void loadConfig(String path) throws DocumentException {
            
            Document document = XMLUtils.parse(Thread.currentThread().getContextClassLoader().getResource(path).getPath());
            Element sqls = document.getRootElement();
            Iterator<Element> moduleIterator = sqls.elementIterator("module");
            while(moduleIterator.hasNext()) {
                Element module = moduleIterator.next();
                String namespace = module.attributeValue("namespace");
                if(namespace == null) {
                    put("", module.elementIterator("sql"));
                } else {
                    namespace = namespace.trim(); //去除头尾的空白字符
                    if("".equals(namespace)) {
                        throw new RuntimeException("配置文件配置错误，module的namespace要么不配置，配置了属性值就不能为空");
                    } else {
                        put(namespace, module.elementIterator("sql"));
                    }
                }
            }
        }
        
        public void put(String namespace,Iterator<Element> sqlIterator) {
            
            while(sqlIterator.hasNext()) {
                Element sql = sqlIterator.next();
                String name = sql.attributeValue("name");
                String value = sql.attributeValue("value");
                if(name==null) {
                    throw new RuntimeException("配置文件配置错误，sql的name属性必须配置");
                }
                name = name.trim(); //去除头尾的空白字符
                if("".equals(name)) {
                    throw new RuntimeException("配置文件配置错误，sql的name属性值不能为空");
                }
                if(namespace.equals("")) {
                    map.put(name,getValue(sql));
                } else {
                    map.put(String.format("%s.%s",namespace,name),getValue(sql));
                }
            }
        }
        
        private String getValue(Element sql) {
            
            String value = sql.attributeValue("value");
            if(value == null) {
                value = sql.getText();
            }
            value = value.trim();
            if("".equals(value)) {
                throw new RuntimeException("配置文件配置错误，value属性值为空");
            }
            return value;
        }
        
        public String get(String key) {
            return map.get(key);
        }
    }
}

