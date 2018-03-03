import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Zhihu {
	
	private URL url;
	public Zhihu(String url) throws MalformedURLException {
		
		this.url = new URL(url);
	}
	public Zhihu(URL url) {
		
		this.url = url;
	}
	public List<String> getInfo() throws IOException {
		
		List<String> list = new ArrayList<String>();
		String HTMLInfo = Spider.get(url);	//获取网页信息
		 // 用来匹配标题  
        Pattern questionPattern = Pattern.compile("question_link.+?>(.+?)<");  
        Matcher questionMatcher = questionPattern.matcher(HTMLInfo);  
        // 用来匹配url，也就是问题的链接  
        Pattern urlPattern = Pattern.compile("question_link.+?href=\"(.+?)\"");  
        Matcher urlMatcher = urlPattern.matcher(HTMLInfo); 
        String format = "问题：{0};\n超链接：{1};\n描述：{3}\n\n\n";
        String content = null;
		while(questionMatcher.find()&&urlMatcher.find()) {

			content = MessageFormat.format(format,questionMatcher.group(1),urlMatcher.group(1));
			list.add(content);
		}
		return list;
	}
}
