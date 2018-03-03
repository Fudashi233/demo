package cn.edu.jxau.tag;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;


public class GreetTag extends BodyTagSupport
{
	private int count;
	private static final String USER = "Clive";
	public void setCount(int count)
	{
		this.count = count;
	}
	@Override
	public int doStartTag()
	{
		if(count<=0)
		{
			return SKIP_BODY;
		}
		else
		{
			return EVAL_BODY_BUFFERED;
		}	
	}
	@Override
	public int doAfterBody()
	{
		if(count>1)
		{
			count--;
			return EVAL_BODY_AGAIN;
		}
		else
		{
			return SKIP_BODY;
		}
	}
	@Override 
	public int doEndTag()
	{
		String username = pageContext.getRequest().getParameter("username");
		String content = bodyContent.getString();
		JspWriter writer = pageContext.getOut();
		if(USER.equals(username))
		{
			content = "Go away,Clive";
		}
		try 
		{
			writer.println(content);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}
	
}
