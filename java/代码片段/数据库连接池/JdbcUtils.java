package cn.edu.jxau.test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcUtils {

    private static ConnectionPool pool = new ConnectionPool();

    public JdbcUtils() {
        throw new RuntimeException("不可实例化JdbcUtils");
    }

    public static Connection getConnection() throws SQLException {
        return pool.getConnection();
    }

    public static void close(Connection connection, Statement statement, ResultSet resultSet) throws SQLException {

        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException e) {
            throw new SQLException("resultSet关闭失败", e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                throw new SQLException("statement关闭失败", e);
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    throw new SQLException("connection关闭失败", e);
                }
            }
        }
    }
}
