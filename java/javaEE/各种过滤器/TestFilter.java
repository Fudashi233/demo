package cn.edu.jxau.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpServletResponse;

public class TestFilter implements Filter
{
	public void init(FilterConfig config)
	{
		
	}
	public void destroy()
	{
		
	}
	public void doFilter(ServletRequest request,ServletResponse response,FilterChain chain) throws IOException, ServletException
	{
		ResponseWrapper responseWrapper = new ResponseWrapper((HttpServletResponse)response);
		chain.doFilter(request,responseWrapper);
	}
}
class ResponseWrapper extends HttpServletResponseWrapper
{
	public ResponseWrapper(HttpServletResponse response) throws IOException
	{
		super(response);
		PrintWriter writer = response.getWriter();
		writer.println("HuHuHu");
		System.out.println("HuHuHu");
	}
}