package cn.edu.jxau.tag;

import javax.servlet.jsp.tagext.TagSupport;

import java.io.IOException;
import java.util.Properties;

public class MessageTag extends TagSupport
{
	private String key;
	public void setKey(String key)
	{
		this.key = key;
	}
	public String geyKey(String key)
	{
		return this.key;
	}
	@Override
	public int doEndTag()
	{
		String message = "";
		String language = (String)(pageContext.findAttribute("language"));
		Properties ps = null;
		if("Chinese".equalsIgnoreCase(language))
		{
			ps = (Properties)(pageContext.findAttribute("ps_ch"));
		}
		else
		{
			ps = (Properties)(pageContext.findAttribute("ps"));
		}
		message = ps.getProperty(key);
		try
		{
			pageContext.getOut().print(message);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return TagSupport.EVAL_PAGE;
	}
}
