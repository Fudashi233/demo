package cn.edu.jxau.filter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.Iterator;

public class ByteFilter implements Filter
{
	private Properties ps;
	@Override
	public void init(FilterConfig config)
	{
		String dir = config.getInitParameter("dir");
		ServletContext servletContext = config.getServletContext();
		InputStream input = servletContext.getResourceAsStream(dir);
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
				{
					input.close();
				}
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
			}
		}
	}
	@Override
	public void doFilter(ServletRequest request,ServletResponse response,FilterChain chain) throws IOException, ServletException
	{
		ByteServletResponse byteServletResponse = new ByteServletResponse((HttpServletResponse)response,ps);
		chain.doFilter(request,byteServletResponse);
	}
	@Override
	public void destroy()
	{
		
	}
}


class ByteServletResponse extends HttpServletResponseWrapper
{
	ByteServletOutputStream output;
	public ByteServletResponse(HttpServletResponse response,Properties ps)
	{
		super(response);
		try 
		{
			ServletOutputStream X = response.getOutputStream();
			output = new ByteServletOutputStream(X,ps);
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	@Override
	public ServletOutputStream getOutputStream() throws IOException
	{
		return this.output;
	}
}


class ByteServletOutputStream extends ServletOutputStream
{
	private Properties ps;
	private boolean isClosed;
	private ByteArrayOutputStream byteOutputStream;
	private ServletOutputStream servletOutputStream;
	public ByteServletOutputStream(ServletOutputStream servletOutputStream,Properties ps)
	{
		this.ps = ps;
		isClosed = false;
		byteOutputStream = new ByteArrayOutputStream();
		this.servletOutputStream = servletOutputStream;
	}
	@Override
	public void write(int b)
	{
		byteOutputStream.write(b);
	}
	public void close() throws IOException
	{
		
	}
	public void flush() throws IOException
	{
		
	}
	public void process() throws IOException
	{
	
	
	}
}