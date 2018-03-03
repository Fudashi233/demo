package cn.edu.jxau.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

import javax.sql.DataSource;

/**
 * 数据库连接池
 * @author 付大石
 */
public class ConnectionPool implements DataSource {

    private static ConcurrentLinkedQueue<Connection> pool;

    static {

        pool = new ConcurrentLinkedQueue<>();
        try (InputStream in = ConnectionPool.class.getClassLoader().getResourceAsStream("jdbc.properties");) {
            Properties prop = new Properties();
            prop.load(in);
            String driver = prop.getProperty("driver");
            String url = prop.getProperty("url");
            String username = prop.getProperty("username");
            String password = prop.getProperty("password");
            int initialPoolSize = Integer.parseInt(prop.getProperty("initialPoolSize"));
            Class.forName(driver);

            // 初始化initialPoolSize个数据库连接 //
            for (int i = 0; i < initialPoolSize; i++) {
                pool.offer(DriverManager.getConnection(url, username, password));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Connection getConnection() throws SQLException {

        if (pool.isEmpty()) {
            throw new SQLException("connection pool is empty");
        }
        Connection connection = pool.poll();
        return (Connection) Proxy.newProxyInstance(this.getClass().getClassLoader(),
                connection.getClass().getInterfaces(), new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        if (method.getName().equals("close")) { // 将数据库连接归还给数据库连接池
                            pool.offer(connection);
                            return null;
                        } else {
                            return method.invoke(connection, args);
                        }
                    }
                });
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return null;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }
}
