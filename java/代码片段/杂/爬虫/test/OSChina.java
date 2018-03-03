import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

public class OSChina {

	public static void main(String[] args) throws IOException {
		
		OSChina site = new OSChina();
		site.featch();
	}
	
	private final String URL = "http://www.oschina.net/news/list";
	private Elements elementList;
	public OSChina() throws IOException {
		
		elementList = init();
	}
	private Elements init() throws IOException {
		
		Document doc = Jsoup.connect(URL).userAgent("Mozilla/5.0 (Windows NT 6.1; rv:30.0) Gecko/20100101 Firefox/30.0").get();
		return doc.select("#RecentNewsList .List >li");
	}
	public void featch() {
		
		for(int i=0,size=elementList.size();i<size;i++) {
			
			Element element = elementList.get(i);
			System.out.println(getTitle(element));
			System.out.println(getInfo(element));
			System.out.println(getArticle(element));
			System.out.println("-----------------");
		}
	}
	private String getTitle(Element li) {
		
		Element h2 = li.getElementsByTag("h2").get(0);
		return h2.text();
	}
	private String getInfo(Element li) {
		
		Element p = li.getElementsByTag("p").get(0);
		TextNode node = (TextNode) p.getElementsByTag("a").get(0).nextSibling();
		StringBuilder builder = new StringBuilder();
		builder.append("作者:");
		builder.append(p.getElementsByTag("a").get(0).text());
		builder.append("\n");
		builder.append("信息:");
		builder.append(p.getElementsByTag("a").get(0).nextSibling());
		return builder.toString();
	}
	private String getArticle(Element li) {
		
		Element p = li.getElementsByTag("p").get(1);
		return p.text();
	}
}
