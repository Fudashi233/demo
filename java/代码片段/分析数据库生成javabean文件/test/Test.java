package cn.edu.jxau.test;

import java.sql.SQLException;



public class Test {

    public static void main(String[] args) throws SQLException {
        
        DatabaseAnalyse analyser = new DatabaseAnalyse("jdbc:mysql://localhost/my?serverTimezone=UTC",
                "root", "root");
        analyser.analyse("C:\\Users\\PC-Clive\\Desktop","cn.edu.jxau.bean",new TableNameHandler() {

            /**
             * 数据库中的表名都是t_xxx的形式，比如t_user,t_article
             * 在这利用{@code handle()}方法将其转换成User、Article形式
             */
            @Override
            public String handle(String tableName) {
                char ch = tableName.charAt(2);
                ch = Character.toUpperCase(ch);
                return ch+tableName.substring(3);
            }
        });
        analyser.close();
    }
}
