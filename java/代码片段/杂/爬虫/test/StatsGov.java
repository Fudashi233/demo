import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class StatsGov {

	public static void main(String[] args) {
		
		StatsGov spider = null;
		try {
			
			spider = new StatsGov();
			spider.featch();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	private String URLStr = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2013/36.html";//中国国家统计局网址
	private String filePath = "C:/Users/PC-Clive/Desktop/info.txt";
	private Map<Integer,String> map;
	private BufferedWriter writer;
	private Document doc;
	
	public StatsGov() throws IOException {
		
		doc = init();
	}
	
	private Document init() throws IOException {
		
		map = new HashMap<Integer,String>();
		map.put(Level.PROVINCE,".provincetr");
		map.put(Level.CITY,".citytr");
		map.put(Level.COUNTY,".countytr");
		map.put(Level.TOWN,".towntr");
		map.put(Level.VILLAGE,".village");
		
		writer = new BufferedWriter(new FileWriter(new File(filePath)));
		return getDocument();
	}
	
	private void write(Element element,int level) throws IOException {
		
		String info = element.text();
		write(info,level);
	}
	
	private void write(String info,int level) throws IOException {
		
		for(int i=1;i<level;i++) {
			
			writer.write("\t");
		}
		writer.write(info);
		System.out.println("65---"+info);
		writer.newLine();
		writer.flush();
	}
	
	private Document getDocument() throws IOException {
		
		return Jsoup.connect(URLStr).timeout(1000*10).get();
	}
	
	public void featch() throws IOException {
		
		//获取所有的省
		Elements cityList = doc.select(map.get(Level.CITY));
		for(int i=0,size=cityList.size();i<size;i++) {
			
			Element city = cityList.get(i).child(1);
			write(city.text(),Level.CITY);
			String URLStr = city.child(0).absUrl("href");
			featch(URLStr,Level.COUNTY);
		}
		try {
			
			
		} finally {
			
			if(writer!=null) {
				
				System.out.println("92---");
				writer.close();
			}
		}
	}
	
	private void featch(String URLStr,int level) throws IOException {
		
		if(level>Level.VILLAGE) {
			
			return ;
		}
		try {
			
			Thread.sleep(500);
		} catch (InterruptedException e) {
			
			throw new RuntimeException("暂停失败");
		}
		Document tempDoc = Jsoup.connect(URLStr).timeout(1000*10).get();
		Elements elementList = tempDoc.select(map.get(level));
		for(int i=0,size=elementList.size();i<size;i++) {
			
			Element element = elementList.get(i).getElementsByTag("td").get(1);
			String tempURLStr = element.childNode(0).absUrl("href");
			if(tempURLStr!=null && !"".equals(tempURLStr))  {
				
				write(element,level);
				featch(tempURLStr,level+1);
			}
		}
	}
}
interface Level {
	
	public static int PROVINCE = 0;
	public static int CITY = 1;
	public static int COUNTY = 2;
	public static int TOWN = 3;
	public static int VILLAGE = 4;
}
