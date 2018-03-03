import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

public class Test {
	
	public static void main(String[] args) {
		
		
//		<a class="question_link" href="/question/46866160/answer/103316856" data-id="10960466">
//		为什么同样的时速同样的弯道低档位操控更好？
//		</a>
		
		try {
			Zhihu page = new Zhihu("http://www.zhihu.com/explore/recommendations");
			List<String> list = page.getInfo();
			for(int i=0,size=list.size();i<size;i++) {
				
				System.out.println(list.get(i));
			}
			
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
}
