package cn.edu.jxau.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class DatabaseAnalyse {

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private Map<String, String> tableBeanMap; // table name和bean name的映射关系
    private Map<String, ResultSetMetaData> beanMetaDataMap; // bean name和result
                                                            // set meta
                                                            // data的映射关系
    private static final String NEW_LINE;
    private TableNameHandler defaulHandler;

    static {
        NEW_LINE = System.getProperty("line.separator");
    }

    public DatabaseAnalyse(String URL, String username, String password) throws SQLException {

        connection = DriverManager.getConnection(URL, username, password);
        tableBeanMap = new HashMap<>();
        beanMetaDataMap = new HashMap<>();
        defaulHandler = new DefaultHandler();
    }
    
    private static class DefaultHandler implements TableNameHandler {

        @Override
        public String handle(String tableName) {
            return tableName; //默认与表名一致
        }
    }
    
    public File analyse(String path,String packageInfo) throws SQLException {
        return analyse(path,packageInfo,defaulHandler);
    }
    
    public File analyse(String path, String packageInfo,TableNameHandler tableHandler) throws SQLException {

        // 构造文档结构 //
        File dir = new File(path, packageInfo.replace(".", File.separator));
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 构造映射关系 //
        Statement statement = connection.createStatement();
        resultSet = statement.executeQuery("SHOW TABLES");
        while (resultSet.next()) {
            String tableName = resultSet.getString(1);
            String beanName = tableHandler.handle(tableName);
            tableBeanMap.put(tableName, beanName); // 表名与javabean类名的映射
            try (Statement tempStatement = connection.createStatement();
                    ResultSet tempResultSet = tempStatement.executeQuery("SELECT * FROM " + tableName + " LIMIT 1")) {
                beanMetaDataMap.put(beanName, tempResultSet.getMetaData()); // javabean类名与表中属性的映射
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        // 根据数据库表结构为每个javabean构造java文件 //
        Iterator<Entry<String, String>> iterator = tableBeanMap.entrySet().iterator();
        while (iterator.hasNext()) {
            analyse(dir, packageInfo, iterator.next().getValue());
        }

        File file = new File(path, packageInfo.replace(".", "//"));
        return file;
    }

    private File analyse(File dir, String packageInfo, String bean) throws SQLException {

        File file = new File(dir, bean + ".java");
        System.out.println("generate java file:" + file.getAbsolutePath());
        try (FileWriter fileWriter = new FileWriter(file);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(generateJavaFile(packageInfo, bean));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return file;
    }

    private String generateJavaFile(String packageInfo, String bean) throws SQLException {

        StringBuilder javaFile = new StringBuilder();
        StringBuilder fieldBuilder = new StringBuilder();
        StringBuilder GSBuilder = new StringBuilder(); // getter and sette string builder;
        StringBuilder toStringBuilder = new StringBuilder();
        List<String> importList = new ArrayList<>();

        // 包信息 //
        javaFile.append("package").append(" ").append(packageInfo).append(";").append(NEW_LINE);
        javaFile.append(NEW_LINE);
        int importPos = javaFile.length(); //import position，记录导包的位置

        // 类名 //
        javaFile.append("public class").append(" ").append(bean).append(" ").append("{").append(NEW_LINE);
        javaFile.append(NEW_LINE);

        // 构造[数据域]、[getter和setter]、[toString主体] //
        ResultSetMetaData metaData = beanMetaDataMap.get(bean);
        for (int i = 0; i < metaData.getColumnCount(); i++) {
            String className = metaData.getColumnClassName(i + 1);
            String type = className.substring(className.lastIndexOf(".") + 1);
            String fieldName = metaData.getColumnName(i+1);
            buildField(fieldBuilder,type,fieldName,importList);
            buildGS(GSBuilder,type,fieldName); //build getter and setter
            buildToString(toStringBuilder,fieldName,i);
        }
        toStringBuilder.deleteCharAt(toStringBuilder.length()-1); //删除最后一个逗号
        
        // 导包 //
        if (!importList.isEmpty()) { // 如果importList不为空则需要导包
            StringBuilder importBuilder = new StringBuilder();
            for (String importPackage : importList) {
                importBuilder.append(importPackage).append(NEW_LINE);
            }
            javaFile.insert(importPos, importBuilder + NEW_LINE);
        }

        // 数据域 //
        javaFile.append(fieldBuilder.toString()).append(NEW_LINE);
        
        // getter and setter //
        javaFile.append(GSBuilder.toString()).append(NEW_LINE);

        // toString() //
        javaFile.append("    ") // 四空格缩进
                .append("@Override").append(NEW_LINE)
                .append("    ") // 四空格缩进
                .append("public String toString() {").append(NEW_LINE)
                .append("    ").append("    ") // 八空格缩进
                .append("return").append(" ").append("\"").append(bean).append(" ").append("[")
                .append(toStringBuilder.toString())
                .append("]").append("\"").append(";").append(NEW_LINE)
                .append("    ") // 四空格缩进
                .append("}").append(NEW_LINE);

        javaFile.append("}"); // 最后的花括号
        return javaFile.toString();
    }

    /**
     * 构造数据域字符串，importList用于存储需要导包的数据类型
     * 
     * @param fieldBuilder
     * @param type
     * @param fieldName
     * @param importList
     */
    private void buildField(StringBuilder fieldBuilder, String type, String fieldName, List<String> importList) {

        fieldBuilder.append("    ") // 四空格缩进
                .append("private").append(" ");
        if (type.equals("Timestamp") || type.equals("Date") || type.equals("Time")) { // date相关的类特殊处理，统一当做java.util.Date处理
            importList.add("import java.util.Date;");
            fieldBuilder.append("Date");
        } else {
            fieldBuilder.append(type);
        }
        fieldBuilder.append(" ") // 数据域类型与数据域名称之间的空格
                .append(fieldName).append(";").append(NEW_LINE);
    }
    
    
    private void buildGS(StringBuilder GSBuilder, String type, String fieldName) {
        
        if (type.equals("Timestamp") || type.equals("Date") || type.equals("Time")) { // date相关的类特殊处理，统一当做java.util.Date处理
            GSBuilder.append(getSetter("Date", fieldName)).append(NEW_LINE).append(getGetter("Date", fieldName))
                    .append(NEW_LINE);
        } else {
            GSBuilder.append(getSetter(type, fieldName)).append(NEW_LINE).append(getGetter(type, fieldName))
                    .append(NEW_LINE);
        }
    }
    
    private String getGetter(String type, String fieldName) {

        StringBuilder getterBuilder = new StringBuilder();
        getterBuilder.append("    ").append(String.format("public %s get%s() {", type, specialForJavaBean(fieldName)))
                .append(NEW_LINE).append("    ").append("    ").append(String.format("return %s;", fieldName))
                .append(NEW_LINE).append("    ").append("}");
        return getterBuilder.toString();

    }

    private String getSetter(String type, String fieldName) {

        StringBuilder getterBuilder = new StringBuilder();
        getterBuilder.append("    ")
                .append(String.format("public void set%s(%s %s) {", specialForJavaBean(fieldName), type, fieldName))
                .append(NEW_LINE).append("    ").append("    ")
                .append(String.format("this.%s = %s;", fieldName, fieldName)).append(NEW_LINE).append("    ")
                .append("}");
        return getterBuilder.toString();
    }

    /**
     * 根据javabean规范，生成对应的方法名
     * @param fieldName
     */
    private String specialForJavaBean(String fieldName) {

        if (fieldName.length() <= 1) { // 长度为1
            return fieldName.toUpperCase();
        }
        char c1 = fieldName.charAt(0);
        char c2 = fieldName.charAt(1);
        if (Character.isUpperCase(c1) && Character.isUpperCase(c2)) {
            // nothing to do
        } else if (Character.isLowerCase(c1) && Character.isUpperCase(c2)) {
            // nothing to do
        } else if (Character.isUpperCase(c1) && Character.isLowerCase(c2)) {
            throw new RuntimeException(String.format("the %s does not meet java bean specification", fieldName));
        } else if (Character.isLowerCase(c1) && Character.isLowerCase(c2)) {
            return Character.toUpperCase(c1) + fieldName.substring(1);
        }
        return fieldName;
    }
    
    /**
     * 构造toString()方法主体的字符串，index指定当前数据域的索引，用于换行
     * 
     * @param toStringBuilder
     * @param fieldName
     * @param index
     */
    private void buildToString(StringBuilder toStringBuilder,String fieldName,int index) {
        
        if (index != 0 && index % 3 == 0) { // 换行
            toStringBuilder.append(String.format("%s=\"", fieldName)).append(NEW_LINE);
            toStringBuilder.append("    ").append("    ") // 八空格缩进
                    .append(String.format(" + %s + \",", fieldName));
        } else {
            toStringBuilder.append(String.format("%s=\" + %s + \",", fieldName, fieldName));
        }
    }

    public void close() {

        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
