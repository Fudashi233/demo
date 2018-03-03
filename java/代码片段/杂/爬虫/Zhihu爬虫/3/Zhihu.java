import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Zhihu {
	
	private String question;// 问题  
	private String description;// 问题描述  
    private URL ZhihuURL;// 网页链接  
    private List<String> answerList;// 存储所有回答的数组  
    private final String QUESTION_URL_STR = "http://www.zhihu.com/question/";	//question URL string
    private String HTMLInfo;
    public Zhihu(String URLStr) throws IOException {
    	
    	ZhihuURL = this.getQuestionURL(URLStr);
    	if(ZhihuURL!=null) {
    		
    		HTMLInfo = this.getHTMLInfo();
    		question = this.getQuestion();
    		description = this.getDescription();
    		answerList = this.getAnswerList();
    		
    	} else {
    		
    		throw new RuntimeException("URLStr不正常，请检查");
    	}
    }
    
    
    /**
     * 将http://www.zhihu.com/question/22355264/answer/21102139或者/question/19868577/answer/35629462
     * 转化成http://www.zhihu.com/question/22355264,否则不变  
     * 
     * @param URLStr
     * @return
     * @throws MalformedURLException 
     */
    private URL getQuestionURL(String URLStr) throws MalformedURLException {
    	
    	Pattern pattern = Pattern.compile("question/(.*?)/");
    	Matcher matcher = pattern.matcher(URLStr); 
    	while(matcher.find()) {
    		
    		URL url = new URL(QUESTION_URL_STR+matcher.group(1));
    		System.out.println("49---"+url);
    		return url;
    	}
    	return null;
    }
    
    private String getHTMLInfo() throws IOException {
    	
    	String HTMLInfo = Spider.get(this.ZhihuURL);
    	return HTMLInfo;
    }
    
    private String getQuestion() {
    	
    	Pattern pattern = Pattern.compile("zh-question-title.+?<h2.+?><span class=\"zm-editable-content\">(.+?)</span></h2>");
    	Matcher matcher = pattern.matcher(this.HTMLInfo);
    	while(matcher.find()) {
    		
    		return matcher.group(1);
    	}
    	return null;
    }
    
    private String getDescription() {
    	
    	Pattern pattern = Pattern.compile("zh-question-detail.+?<div.+?>(.*?)</div>");
    	Matcher matcher = pattern.matcher(this.HTMLInfo);
    	while(matcher.find()) {
    		
    		return matcher.group(1);
    	}
    	return null;
    }
    private List<String> getAnswerList() {
    	
    	List<String> list = new ArrayList<String>();
    	Pattern pattern = Pattern.compile("/answer/content.+?<div.+?>(.*?)</div>");
    	Matcher matcher = pattern.matcher(this.HTMLInfo);
    	while(matcher.find()) {
    		
    		list.add(matcher.group(1));
    	}
    	return list;
    }
    
    public void print() {
    	
    	System.out.println("↓----------------");
    	System.out.println("Question:"+question);
    	System.out.println("Description:"+description);
    	System.out.println("AnswerList(size="+answerList.size()+"):");
    	for(int i=0,size=answerList.size();i<size;i++) {
    		
    		System.out.println(answerList.get(i)+"\n\n\n");
    	}
    	System.out.println("↑----------------\n");
    }
}
