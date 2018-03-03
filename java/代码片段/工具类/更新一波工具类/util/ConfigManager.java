package cn.edu.jxau.util;

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
}
