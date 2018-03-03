package cn.edu.jxau.tag;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.SkipPageException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class Referer extends SimpleTagSupport
{
	private String site;
	private String page;
	public void setSite(String site)
	{
		this.site = site;
	}
	public void setPage(String page)
	{
		this.page = page;
	}
	@Override
	public void doTag() throws IOException, SkipPageException
	{
		PageContext pageContext = (PageContext) this.getJspContext();
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		String referer = request.getHeader("referer");
		System.out.println("30--- "+referer);
		if(referer==null||!referer.startsWith(site))
		{
			String webRoot = request.getContextPath();
			HttpServletResponse response = (HttpServletResponse)pageContext.getResponse();
			if(page.startsWith(webRoot))
			{
				response.sendRedirect(page);
			}
			else
			{
				response.sendRedirect(webRoot+"/"+page);
			}
			throw new SkipPageException();
		}
	}
}
