package cn.edu.jxau.filter;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class CharFilter implements Filter
{
	private Properties ps;
	@Override
	public void init(FilterConfig config)
	{
		String dir = config.getInitParameter("dir");
		ServletContext context = config.getServletContext();
		InputStream input = context.getResourceAsStream(dir);
		ps = new Properties();
		try 
		{
			ps.load(input);
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(input!=null)
					input.close();
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
			}
		}
	}
	@Override
	public void destroy()
	{
		
	}
	@Override
	public void doFilter(ServletRequest request,ServletResponse response,FilterChain chain) throws IOException, ServletException
	{
		CharResponseWrapper responseWrapper = new CharResponseWrapper((HttpServletResponse)response);
		chain.doFilter(request,responseWrapper);
		String content = responseWrapper.getCharArray().toString();
		Enumeration<?> enumeration = ps.keys();
		String temp = "";
		while(enumeration.hasMoreElements())
		{
			temp = (String)enumeration.nextElement();
			content = content.replaceAll(temp,ps.getProperty(temp));
		}
		response.getWriter().println(content);
	}
}
class CharResponseWrapper extends HttpServletResponseWrapper
{
	private CharArrayWriter charArray;
	public CharResponseWrapper(HttpServletResponse response)
	{
		super(response);
		charArray = new CharArrayWriter();
	}
	@Override
	public PrintWriter getWriter()
	{
		return new PrintWriter(charArray);
	}
	public CharArrayWriter getCharArray()
	{
		return charArray;
	}
}
