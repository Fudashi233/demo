package cn.edu.jxau.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class LogUtil {
    private static Logger logger = null;
    private static Logger logging = Logger.getLogger(LogUtil.class.getName());
    private static final String LINE_SPERATOR;
    static {
    	
    	LINE_SPERATOR = System.getProperty("line.separator");
        if (null == logger) {
            InputStream is  = LogUtil.class.getClassLoader().getResourceAsStream("logger.properties");
            try {
                LogManager.getLogManager().readConfiguration(is);
            } catch (SecurityException e) {
            	
            	logging.warning(e.getMessage());
			} catch (IOException e) {

				logging.warning(e.getMessage());
			} finally{
                try {
                    is.close();
                } catch (IOException e) {
                    logging.warning("无法关闭logger.properties\n" + e.getMessage());
                }
            }
            logger = Logger.getLogger("LOGGER");
        }
    }
    public static void severe(String msg) {
    	
    	logger.severe(format(msg));
    }
    public static void warning(String msg) {
    	
    	logger.warning(format(msg));
    }
    public static void info(String msg) {
    	
    	logger.info(format(msg));
    }
    public static void config(String msg) {
    	
    	logger.config(format(msg));
    }
    public static void fine(String msg) {
    	
    	logger.fine(format(msg));
    }
    public static void finer(String msg) {
    	
    	logger.finer(format(msg));
    }
    public static void finest(String msg) {
    	
    	logger.finest(format(msg));
    }
    private static int getLineNumber() {
    	
    	return Thread.currentThread().getStackTrace()[4].getLineNumber();
    }
    
    public static String getClassName() {
		
		return Thread.currentThread().getStackTrace()[4].getClassName();
	}
	
	public static String getMethodName() {
		
		return Thread.currentThread().getStackTrace()[4].getMethodName();
	}
    private static String format(String message) {
    	
    	StringBuilder stringBuilder = new StringBuilder();
    	stringBuilder.append(message).append(LINE_SPERATOR);
    	stringBuilder.append("类名：").append(getClassName()).append(LINE_SPERATOR);
    	stringBuilder.append("方法：").append(getMethodName()).append(LINE_SPERATOR);
    	stringBuilder.append("行号：").append(getLineNumber()).append(LINE_SPERATOR);
    	stringBuilder.append("--------------------------------------");
    	return stringBuilder.toString();
    }
}
