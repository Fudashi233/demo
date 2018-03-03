package cn.edu.jxau.web.servlet;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import cn.edu.jxau.bean.Action;
import cn.edu.jxau.bean.Result;
import cn.edu.jxau.utils.LogUtil;
import cn.edu.jxau.utils.XMLUtil;

public class InitServlet extends HttpServlet {

	
	public void init() {
		
		String path = this.getServletContext().getRealPath("");
		try {
			/*加载power.xml,用于权限控制*/
			Map<Integer,List<String>> powerMap = this.loadPowerXML(path,this.getInitParameter("powerConfig"));
			if(powerMap==null) {
				
				throw new RuntimeException("权限配置文件加载失败");
			}
			super.getServletContext().setAttribute("powerMap",powerMap);
			/*加载service.xml,用于业务处理*/
			Map<String,List<Action>> serviceMap = this.loadServletXML(path,this.getInitParameter("serviceConfig"));
			if(serviceMap==null) {
				
				throw new RuntimeException("业务配置文件加载失败");
			}
			super.getServletContext().setAttribute("serviceMap",serviceMap);
			
		} catch (DocumentException e) {
			LogUtil.warning(30,"配置文件加载失败");
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
		
		configPath = path+configPath;
		LogUtil.info(58,"开始加载配置文件："+configPath);
		Document doc = XMLUtil.parse(configPath);
		Element root = doc.getRootElement();
		Map<Integer,List<String>> powerMap = new HashMap<Integer,List<String>>();
		List<Element> list = root.elements("role");
		
		Iterator<Element> roleIterator = list.iterator();
		while(roleIterator.hasNext()) {
			
			Element role = roleIterator.next();
			int roleID = Integer.parseInt(role.attribute("roleID").getValue());
			powerMap.put(roleID,this.getPageList(role.elements("page")));
		}
		return powerMap;
	}
	private List<String> getPageList(List<Element> pageList) {
		
		Iterator<Element> iterator  = pageList.iterator();
		List<String> list = new LinkedList<String>();
		while(iterator.hasNext()) {
			
			Element pageEle = iterator.next();
			String text = pageEle.element("URL").getText();
			if(text!=null && !"".equals(text.trim())) {
				
				list.add(text);
			}
		}
		return list;
	}
	private Map<String,List<Action>> loadServletXML(String path,String configPath)throws DocumentException {
		
		configPath = path+configPath;
		LogUtil.info(91,"开始加载配置文件："+configPath);
		Document doc = XMLUtil.parse(configPath);
		Element root = doc.getRootElement();
		List<Element> servletList = root.elements("servlet");
		Iterator<Element> servletIterator = servletList.iterator();
		Map<String,List<Action>> map = new HashMap<String,List<Action>>();
		while(servletIterator.hasNext()) {
			
			Element servletELe = servletIterator.next();
			String className = servletELe.attribute("className").getValue();
			map.put(className,getActionList(servletELe.elements("action")));
		}
		return map;
	}
	private List<Action> getActionList(List<Element> actionList) {
		
		Iterator<Element> actionIterator = actionList.iterator();
		List<Action> list = new LinkedList<Action>();
		while(actionIterator.hasNext()) {
			
			Element actionEle = actionIterator.next();
			String methodName = actionEle.attribute("methodName").getValue();
			Action action = new Action();
			action.setMethodName(methodName);
			action.setResultMap(this.getResultMap(actionEle.elements("result")));
			list.add(action);
		}
		return list;
	}
	private Map<String,Result> getResultMap(List<Element> resultList) {
		
		Iterator<Element> resultIterator = resultList.iterator();
		Map<String,Result> map = new HashMap<String,Result>();
		while(resultIterator.hasNext()) {
			
			Element resultEle = resultIterator.next();
			String name = resultEle.attribute("name").getValue();
			String type = resultEle.element("type").getText();
			String destinationURI = resultEle.element("destinationURI").getText();
			Result result = new Result();
			result.setType(type);
			result.setDestinationURI(destinationURI);
			map.put(name,result);
		}
		return map;
	}
}
