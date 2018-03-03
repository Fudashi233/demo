package logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * 日志{@link java.util.logging.Logger}获取器
 */
public class Logging {
    private static Logger logger = null;
    private Logging(){}
     
    public static Logger getLogger(){
        if (null == logger) {
            InputStream is  = Logging.class.getClass().getResourceAsStream("logger.properties");
                   
				   System.out.println( Logging.class.getClassLoader().getResource("logger.properties"));	//file:/E:/eclipseWork/Test/bin/logger.properties
            System.out.println(Logging.class.getClassLoader().getResource("/logger.properties"));	//null
            System.out.println(Logging.class.getClass().getResource("logger.properties"));			//null
            System.out.println(Logging.class.getClass().getResource("/logger.properties"));			//file:/E:/eclipseWork/Test/bin/logger.properties
            	
            
            try {
                LogManager.getLogManager().readConfiguration(is);
            } catch (Exception e) {
                logging.warning("input properties file is error.\n" + e.toString());
            }finally{
                try {
                    is.close();
                } catch (IOException e) {
                    logging.warning("close FileInputStream a case.\n" + e.toString());
                }
            }
             
            logger = Logger.getLogger("LOGGER");
        }
        return logger;
    }
     
    private static Logger logging = Logger.getLogger(Logging.class.getName());
}