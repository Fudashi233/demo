package cn.edu.jxau.web.servlet;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import cn.edu.jxau.utils.LogUtil;
import cn.edu.jxau.utils.XMLUtil;

public class InitServlet extends HttpServlet {

	
	public void init() {
		
		String path = this.getServletContext().getRealPath("");
		/*加载power.xml，用于权限控制*/
		try {
			Map<Integer,List<String>> powerMap = loadPowerXML(path,this.getInitParameter("powerConfig"));
			if(powerMap==null) {
				
				throw new RuntimeException("权限配置文件加载失败");
			}
			this.getServletContext().setAttribute("powerMap",powerMap);
		} catch (DocumentException e) {
			LogUtil.warning(30,"权限配置文件加载失败");
			e.printStackTrace();
		}
	}
	/**
	 * 加载power.xml，用于权限控制
	 * @param path
	 * @param configPath
	 * @return
	 * @throws DocumentException
	 */
	private Map<Integer,List<String>> loadPowerXML(String path,String configPath) throws DocumentException {
		
		configPath = path+File.separator+"WEB-INF"+File.separator+configPath;
		LogUtil.info(44,"开始加载配置文件："+configPath);
		Document doc = XMLUtil.parse(configPath);
		Element root = doc.getRootElement();
		Map<Integer,List<String>> powerMap = new HashMap<Integer,List<String>>();
		List<String> userPageList = new LinkedList<String>();
		List<String> adminPageList = new LinkedList<String>();
		List<String> superAdminPageList = new LinkedList<String>();
		List<Element> list = root.elements("role");
		
		Iterator<Element> roleIterator = list.iterator();
		while(roleIterator.hasNext()) {
			
			Element role = roleIterator.next();
			int roleID = Integer.parseInt(role.attribute("roleID").getValue());
			powerMap.put(roleID,getPageList(role.elements("page")));
		}
		return powerMap;
	}
	private List<String> getPageList(List<Element> page) {
		
		Iterator<Element> iterator  = page.iterator();
		List<String> pageList = new LinkedList<String>();
		while(iterator.hasNext()) {
			
			Element pageEle = iterator.next();
			String text = pageEle.element("URL").getText();
			if(text!=null && !"".equals(text.trim())) {
				
				pageList.add(text);
			}
		}
		return pageList;
	}
}
