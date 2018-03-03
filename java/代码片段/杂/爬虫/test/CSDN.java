import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class CSDN {
	
	private static String LOGIN_URL = "https://passport.cnblogs.com/user/signin";
	private static String INDEX_URL = "https://home.cnblogs.com/";
	private static String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; rv:30.0) Gecko/20100101 Firefox/30.0";
	public static void main(String[] args) throws IOException {
		
	
	}
	private static Map<String,String> getLoginData(String username,String password) throws IOException {
		
		Map<String,String> loginData = new HashMap<String,String>();
		Document doc = Jsoup.connect(LOGIN_URL).userAgent(USER_AGENT).get();
		Elements elementList = doc.select("form input");
		for(int i=0,size=elementList.size();i<size;i++) {
			
			String name = elementList.get(i).attr("name");
			String value = elementList.get(i).attr("value");
			loginData.put(name,value);
		}
		//设置账号、密码
		loginData.put("username",username);
		loginData.put("password",password);
		return loginData;
	}
	private static Map<String,String> getLoginCookie(String username,String password) throws IOException {
		
		return Jsoup.connect(LOGIN_URL).userAgent(USER_AGENT).method(Connection.Method.POST).execute().cookies();
	}
}
