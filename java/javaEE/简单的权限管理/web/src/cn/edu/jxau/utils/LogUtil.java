package cn.edu.jxau.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class LogUtil {
    private static Logger logger = null;
    private static Logger logging = Logger.getLogger(LogUtil.class.getName());
    static {
    	
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
    	
    	logger.severe(msg);
    }
    public static void warning(String msg) {
    	
    	logger.warning(msg);
    }
    public static void info(String msg) {
    	
    	logger.info(msg);
    }
    public static void config(String msg) {
    	
    	logger.config(msg);
    }
    public static void fine(String msg) {
    	
    	logger.fine(msg);
    }
    public static void finer(String msg) {
    	
    	logger.finer(msg);
    }
    public static void finest(String msg) {
    	
    	logger.finest(msg);
    }
    public static void severe(int line,String msg) {
    	
    	if(line<=0)
    		throw new IllegalArgumentException("line不可以为负数");
    	logger.severe(line+"--- "+msg);
    }
    public static void warning(int line,String msg) {
    	
    	if(line<=0)
    		throw new IllegalArgumentException("line不可以为负数");
    	logger.warning(line+"--- "+msg);
    }
    public static void info(int line,String msg) {
    	
    	if(line<=0)
    		throw new IllegalArgumentException("line不可以为负数");
    	logger.info(line+"--- "+msg);
    }
    public static void config(int line,String msg) {
    	
    	if(line<=0)
    		throw new IllegalArgumentException("line不可以为负数");
    	logger.config(line+"--- "+msg);
    }
    public static void fine(int line,String msg) {
    	
    	if(line<=0)
    		throw new IllegalArgumentException("line不可以为负数");
    	logger.fine(line+"--- "+msg);
    }
    public static void finer(int line,String msg) {
    	
    	if(line<=0)
    		throw new IllegalArgumentException("line不可以为负数");
    	logger.finer(line+"--- "+msg);
    }
    public static void finest(int line,String msg) {
    	
    	if(line<=0)
    		throw new IllegalArgumentException("line不可以为负数");
    	logger.finest(line+"--- "+msg);
    }
    public static void severe(int line,Object msg) {
    	
    	if(line<=0)
    		throw new IllegalArgumentException("line不可以为负数");
    	logger.severe(line+"--- "+msg.toString());
    }
    public static void warning(int line,Object msg) {
    	
    	if(line<=0)
    		throw new IllegalArgumentException("line不可以为负数");
    	logger.warning(line+"--- "+msg.toString());
    }
    public static void info(int line,Object msg) {
    	
    	if(line<=0)
    		throw new IllegalArgumentException("line不可以为负数");
    	logger.info(line+"--- "+msg.toString());
    }
    public static void config(int line,Object msg) {
    	
    	if(line<=0)
    		throw new IllegalArgumentException("line不可以为负数");
    	logger.config(line+"--- "+msg.toString());
    }
    public static void fine(int line,Object msg) {
    	
    	if(line<=0)
    		throw new IllegalArgumentException("line不可以为负数");
    	logger.fine(line+"--- "+msg.toString());
    }
    public static void finer(int line,Object msg) {
    	
    	if(line<=0)
    		throw new IllegalArgumentException("line不可以为负数");
    	logger.finer(line+"--- "+msg.toString());
    }
    public static void finest(int line,Object msg) {
    	
    	if(line<=0)
    		throw new IllegalArgumentException("line不可以为负数");
    	logger.finest(line+"--- "+msg.toString());
    }
}
