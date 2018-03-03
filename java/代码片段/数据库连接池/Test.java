package cn.edu.jxau.test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Test {
    
    public static void main(String[] args) throws IOException, SQLException {

        Connection connection = JdbcUtils.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM t_user");
        while(resultSet.next()) {
            int id = resultSet.getInt("id");
            String username = resultSet.getString("username");
            String password = resultSet.getString("password");
            int age = resultSet.getInt("age");
//            System.out.printf("%d,%s,%s,%d\n",id,username,password,age);
        }
        JdbcUtils.close(connection, statement, resultSet);
    }
}


