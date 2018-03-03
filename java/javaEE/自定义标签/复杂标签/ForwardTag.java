package cn.edu.jxau.tag;

import java.io.IOException;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.jsp.tagext.TagSupport;

public class ForwardTag extends TagSupport
{
	private String page;
	public void setPage(String page)
	{
		this.page = page;
	}
	public String getPage()
	{
		return this.page;
	}
	@Override
	public int doStartTag()
	{
		return EVAL_BODY_INCLUDE;
	}
	public int doEndTag()
	{
		HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
		HttpServletResponse response = (HttpServletResponse)pageContext.getResponse();
		try 
		{
			request.getRequestDispatcher(page).forward(request,response);
		} 
		catch (ServletException e)
		{
			e.printStackTrace();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return SKIP_PAGE;
	}
}
