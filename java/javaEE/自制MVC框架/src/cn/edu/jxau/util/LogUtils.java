package cn.edu.jxau.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * 
 * @author 付大石
 */
public class LogUtils {
    
    private static Logger logger;
    private static final String LINE_SPERATOR;
    static {

        LINE_SPERATOR = System.getProperty("line.separator");
        try(InputStream inStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(ConfigManager.getConfig("logger"));) {
            LogManager.getLogManager().readConfiguration(inStream);
            logger = Logger.getLogger(ConfigManager.getConfig("loggerName"));
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
    
    /**
     * the number 4 is a magic number,and {@method getClassName()},
     * {@method getMethodName()} is same.
     * @return
     */
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
        stringBuilder.append("类：").append(getClassName()).append(LINE_SPERATOR);
        stringBuilder.append("方法：").append(getMethodName()).append(LINE_SPERATOR);
        stringBuilder.append("所在行：").append(getLineNumber()).append(LINE_SPERATOR);
        stringBuilder.append("--------------------------------------");
        return stringBuilder.toString();
    }
}
