package utils;

public class PrintUtil {
	
	public static String getBlank() {
		
		return " ";
	}
	public static String getBlank(int count) {
		
		if(count<0)
			throw new IllegalArgumentException("count 需是非负数");
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<count;i++) {
			
			sb.append(getBlank());
		}
		return sb.toString();
	}
	public static String newLine() {
		
		return System.getProperty("line.separator");
	}
}
