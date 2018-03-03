package cn.edu.jxau.filter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPOutputStream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class GZIPFilter implements Filter
{
	@Override
	public void init(FilterConfig config)
	{
	
	}
	@Override
	public void destroy()
	{
		
	}
	@Override
	public void doFilter(ServletRequest request,ServletResponse response,FilterChain chain) throws IOException, ServletException
	{
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		HttpServletResponse httpResponse = (HttpServletResponse)response;
		if(httpRequest.getHeader("accept-encoding").toLowerCase().contains("gzip"))
		{
			GZIPHttpServletResponseWrapper GZIPResponse = new GZIPHttpServletResponseWrapper(httpResponse);
			chain.doFilter(request,GZIPResponse);
			GZIPResponse.finish();
		}
		else
		{
			chain.doFilter(request,response);
		}
	}
}
class GZIPHttpServletResponseWrapper extends HttpServletResponseWrapper
{
	private GZIPServletOutputStream GZIPOutput;
	private PrintWriter writer;
	private HttpServletResponse response;
	public GZIPHttpServletResponseWrapper(HttpServletResponse response)
	{
		super(response);
		this.response = response;
		GZIPOutput = new GZIPServletOutputStream(response);
		try
		{
			writer = new PrintWriter(new OutputStreamWriter(GZIPOutput,"UTF-8"));
		} 
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
	}
	@Override
	public PrintWriter getWriter()
	{
//		if(writer==null)
//		{
//			try
//			{
//				return new PrintWriter(
//							new OutputStreamWriter(
//								new GZIPServletOutputStream(response),"UTF-8"));
//			} 
//			catch (UnsupportedEncodingException e) 
//			{
//				e.printStackTrace();
//			}
//		}
		return writer;
	}
	@Override
	public ServletOutputStream getOutputStream()
	{
//		if(GZIPOutput==null)
//			return new GZIPServletOutputStream((HttpServletResponse)response);
		return GZIPOutput;
	}
	public void finish()
	{
		if(writer!=null)
			writer.close();
		if(GZIPOutput!=null)
			GZIPOutput.close();
	}
}
class GZIPServletOutputStream extends ServletOutputStream
{
	private GZIPOutputStream GZIPOutput;
	private ByteArrayOutputStream byteOutput;
	private HttpServletResponse response;
	public GZIPServletOutputStream(HttpServletResponse response)
	{
		this.response = response;
		byteOutput = new ByteArrayOutputStream();
		try 
		{
			GZIPOutput = new GZIPOutputStream(byteOutput);
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	public void write(int b) throws IOException
	{
		GZIPOutput.write(b);
	}
	public void close()
	{
		try 
		{
			GZIPOutput.finish();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		byte[] content = byteOutput.toByteArray();
		response.setHeader("content-encoding","gzip");
		response.setHeader("content-length",Integer.toString(content.length));
		ServletOutputStream output = null;
		try
		{
			output = response.getOutputStream();
			output.write(content);
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			try
			{
				if(output!=null)
					output.close();
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
			}
			finally
			{
				try
				{
					if(GZIPOutput!=null)
						GZIPOutput.close();
				}
				catch(IOException ex)
				{
					ex.printStackTrace();
				}
				finally
				{
					try
					{
						if(byteOutput!=null)
							byteOutput.close();
					}
					catch(IOException ex)
					{
						ex.printStackTrace();
					}
				}
			}
		}
	}
}