import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Spider {
	
	public static String get(URL url) throws IOException {
		
		return get(url,"UTF-8");
	}
	
	public static String get(String url) throws IOException {  
		
		return get(new URL(url));
	}
	
	public static String get(URL url,String encoding) throws IOException {
		
		System.out.println("开始爬取数据");
		StringBuilder builder = new StringBuilder();
		URLConnection connection = url.openConnection();
		connection.connect();
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),encoding));
		String line = null;
		while ((line = reader.readLine()) != null) {
			
			builder.append(line);
		}
		// 关闭IO流
		try {

		} finally {

			if (reader != null) {

				reader.close();
			}
		}
		return builder.toString();
	}
}
