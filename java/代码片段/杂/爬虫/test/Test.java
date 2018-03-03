import java.io.IOException;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

public class Test {
	public static void main(String[] args) throws IOException {

//	    Map<String, String> loginPageCookies = Jsoup.connect("https://www.pixiv.net/login.php")
//	            .method(Connection.Method.POST).execute().cookies();

//	    String userName = "710106701@qq.com";
//	    String password = "aptx4869";
//
//	    Connection.Response loginResponse = Jsoup.connect("https://www.pixiv.net/login.php")
//	            .data("mode", "login", "pixiv_id", userName, "pass", password, "skip", "1")
//	            .method(Connection.Method.POST).execute();

//	    Map<String, String> userCookies = loginResponse.cookies();

	    System.out.println(Jsoup.connect("https://www.pixiv.net")
	           .get().body());

	}
}