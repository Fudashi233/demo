package cn.edu.jxau.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

public class ConfigManager {
    
    private static Map<String,String> configMap;
    
    static {
        configMap = new HashMap<String, String>();
        Properties prop = new Properties();
        try(InputStream input = JDBCUtils.class.getClassLoader().getResourceAsStream("this.properties");) {
            prop.load(input);
            Iterator<Entry<Object, Object>> iterator = prop.entrySet().iterator();
            while(iterator.hasNext()) {
                Entry<Object,Object> entry = iterator.next();
                configMap.put(entry.getKey().toString(), entry.getValue().toString());
            }
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public static String getConfig(String key) {
        return configMap.get(key);
    }
    
    public static String JDBC() {
        return getConfig("JDBC");
    }
    
    public static String logger() {
        return getConfig("logger");
    }
    public static String sqls() {
        return getConfig("sqls");
    }
    public static String beans() {
        return getConfig("beans");
    }
    public static String services() {
        return getConfig("services");
    }
    public static String dateFormat() {
        return getConfig("dateFormat");
    }
    public static String encoding() {
        return getConfig("encoding");
    }
    public static File tmpDir() {
        
        String tmpDir = getConfig("tmpDir");
        if(tmpDir==null || tmpDir.trim().equals("")) {
            return new File(System.getProperty("java.io.tmpdir"));
        }
        return new File(tmpDir);
    }
    public static String loggerName() {
        return getConfig("loggerName");
    }
    public static int height() {
        return Integer.parseInt(getConfig("height"));
    }
    public static int width() {
        return Integer.parseInt(getConfig("width"));
    }
    public static boolean withLine() {
        return Boolean.parseBoolean(getConfig("withLine"));
    }
    public static int length() {
        return Integer.parseInt(getConfig("JDBC"));
    }
    public static String codes() {
        return getConfig("JDBC");
    }
    
    public static String postfix() {
        return getConfig("do");
    }
    
    public static int sizeThreshold() {
        return Integer.parseInt(getConfig("sizeThreshold"));
    }
}
