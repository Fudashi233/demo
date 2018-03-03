import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
	
	public static void main(String[] args) {
		
//		//获取页面信息
//		String HTMLInfo = null;
//		try {
//			
//			HTMLInfo = Spider.get("https://www.zhihu.com/explore/recommendations");
//		} catch (IOException e) {
//			
//			e.printStackTrace();
//		}
//		
//		//获取所有我需要的问题的链接
//		List<String> questionList = getQuestionList(HTMLInfo);
//		for(int i=0,size=questionList.size();i<size;i++) {
//			
//			System.out.println(questionList.get(i));
//		}
		
		Zhihu z = null;
		try {
			z = new Zhihu("/question/19868577/answer/35629462");
		} catch (IOException e) {
			e.printStackTrace();
		}
		z.print();
		
	}
	
	private static List<String> getQuestionList(String HTMLInfo) {
		
		List<String> questionList = new LinkedList<String>();
		Pattern pattern = Pattern.compile("<h2>.+?question_link.+?href=\"(.+?)\".+?</h2>");
		Matcher matcher = pattern.matcher(HTMLInfo);
		while(matcher.find()) {
			
			questionList.add(matcher.group(1));
		}
		
		return questionList;
	}
}
