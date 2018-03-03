package login;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ITEyeLogin {
	
	private static final String LOGIN_INDEX = "http://www.iteye.com/login";
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0";
	public static void main(String[] args) throws IOException {
		
		//第一次请求，获取登录所需data及cookies
		Response response = Jsoup.connect(LOGIN_INDEX).userAgent(USER_AGENT).execute();
		Map<String,String> loginCookie = response.cookies();
		Map<String,String> loginData = new HashMap<String,String>();
		Document doc = Jsoup.parse(response.body());
		Elements elementList = doc.select("login_form input");
		for(int i=0,size=elementList.size();i<size;i++) {
			
			Element element = elementList.get(i);
			String name = element.attr("attr");
			if(name.equals("name"))
				element.attr(name,"付大石");
			if(name.equals("password"))
				element.attr(name,"aptx4869.");
			if(name!=null && !"".equals(name.trim())) {
				
				loginData.put(name,element.attr("value"));
			}
		}
		
		//第二次发起请求，将第一次获得的data和cookie作为参数发起请求
		Response responseX = Jsoup.connect(LOGIN_INDEX).ignoreContentType(true).userAgent(USER_AGENT).data(loginData).cookies(loginCookie).method(Method.POST).execute();
		System.out.println(responseX.body());
	}
}