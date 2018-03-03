package logger;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Test {

	public static void main(String[] args) {
		
		
		
		
		
		
		 Logger logger = Logging.getLogger();
	        logger.finest("finest");
	        logger.finer("finer");
	        logger.fine("fine");
	        logger.info("info");
	        logger.config("config");
	        logger.warning("warning");
	        logger.severe("severe");
		
		
		/*
		
		Logger logger = Logger.getLogger("logger");
		//Loger�и�Ĭ�ϵ�consoleHandler��û׼��eclipse�����ƣ�,���default handler��level ��INFO����Ӱ����ԣ����Կ���ʹ��setUserParentHandlers��false������handler�ر�
	    Logger parentLogger = logger.getParent();
	    System.out.println(parentLogger.getName());
	    Handler[] handlers = parentLogger.getHandlers();
	    for(int i=0;i<handlers.length;i++) {
	    	
	    	System.out.println(handlers[i].getClass()+"	"+handlers[i].getLevel());
	    }

	    
//		Logger loggerX = Logger.getLogger("logger.Test");
//		
		
		
//		1.ͬ����loggerֻ����һ��
//		System.out.println(logger);
//		Logger loggerX = Logger.getLogger("Logger");
//		System.out.println(loggerX);
//		System.out.println(logger==loggerX);
		
		
//		2.�㼶��ϵ  java.util.loggin.Level;
//		logger.info("info1");
//		logger.setLevel(Level.WARNING);
//		logger.info("info2");
		
		
//		3.Handler
		ConsoleHandler consoleHandler = new ConsoleHandler();
		consoleHandler.setLevel(Level.ALL);
		logger.addHandler(consoleHandler);
		try {
			
			FileHandler fileHandler = new FileHandler("src/logger/test.log");
			fileHandler.setLevel(Level.FINEST);
//			logger.addHandler(fileHandler);
			fileHandler.setFormatter(new SimpleFormatter());
		} catch (SecurityException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		logger.setUseParentHandlers(false);  
		logger.warning("WAEN");
		logger.info("INFO");
		logger.config("config");
		*/
	}
}

//	5.formatter
class LogFormatter extends Formatter {
	
	@Override
	public String format(LogRecord record) {
		
		return ""+record.getLevel();
	}
}