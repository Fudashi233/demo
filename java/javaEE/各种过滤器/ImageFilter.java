package cn.edu.jxau.filter;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.swing.ImageIcon;

import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class ImageFilter implements Filter
{
	private String dir;
	private int paddingRight;
	private int paddingBottom;
	public void init(FilterConfig config)
	{
		ServletContext servletContext = config.getServletContext();
		this.dir = servletContext.getRealPath(config.getInitParameter("dir"));
		System.out.println(dir);
		this.paddingRight = Integer.parseInt(config.getInitParameter("paddingRight"));
		this.paddingBottom = Integer.parseInt(config.getInitParameter("paddingBottom"));
	}
	public void destroy()
	{
		
	}
	public void doFilter(ServletRequest request,ServletResponse response,FilterChain chain) throws IOException, ServletException
	{
		ImageHttpServletResponse imageResponse = new ImageHttpServletResponse((HttpServletResponse)response,dir,paddingRight,paddingBottom);
		chain.doFilter(request,imageResponse);
		imageResponse.finish();
	}
}
class ImageHttpServletResponse extends HttpServletResponseWrapper
{
	private ImageServletOutptuStream output;
	private String dir;
	private int paddingRight;
	private int paddingBottom;
	private HttpServletResponse response;
	public ImageHttpServletResponse(HttpServletResponse response,String dir,int paddingRight,int paddingBottom)
	{
		super(response);
		this.dir = dir;
		this.paddingRight = paddingRight;
		this.paddingBottom = paddingBottom;
		this.response = response;
		output = new ImageServletOutptuStream();
	}
	@Override
	public ServletOutputStream getOutputStream()
	{
		return output;
	}
	public void finish()
	{
		byte[] imageData  = output.getByteOutput().toByteArray();
		imageData = ImageUtil.waterMark(imageData,dir,paddingRight,paddingBottom);
		response.setContentLength(imageData.length);
		try 
		{
			response.getOutputStream().write(imageData);
			output.close();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
class ImageServletOutptuStream extends ServletOutputStream
{
	private ByteArrayOutputStream byteOutput;
	public ImageServletOutptuStream()
	{
		byteOutput = new ByteArrayOutputStream();
	}
	@Override
	public void write(int b)
	{
		byteOutput.write(b);
	}
	public ByteArrayOutputStream getByteOutput()
	{
		return byteOutput;
	}
}
class ImageUtil
{
	public static byte[] waterMark(byte[] imageData,String dir,int paddingRight,int paddingBottom)
	{
		ImageIcon image = new ImageIcon(imageData);
		ImageIcon mark = null;
		try
		{
			mark = new ImageIcon(ImageIO.read(new File(dir)));
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		int imageWidth = image.getIconWidth();
		int imageHeight = image.getIconHeight();
		System.out.println(imageWidth+" "+imageHeight);
		int markWidth = mark.getIconWidth();
		int markHeight = mark.getIconHeight();
		System.out.println(markWidth+" "+markHeight);
		BufferedImage bufferedImage = new BufferedImage(imageWidth,imageHeight,BufferedImage.TYPE_INT_RGB);
		Graphics g = bufferedImage.getGraphics();
		g.drawImage(image.getImage(),0,0,imageWidth,imageHeight,null);
		
		//如果图片大小合适，则设置标记水印
		if(imageWidth-markWidth-paddingRight-paddingRight>0)
		{
			if(imageHeight-markHeight-paddingBottom-paddingBottom>0)
			{
				g.drawImage(mark.getImage(),imageWidth-markWidth-paddingRight,
								imageHeight-markHeight-paddingBottom,
									markWidth,markHeight,null);
			}
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		try 
		{
			encoder.encode(bufferedImage);
		}
		catch (ImageFormatException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		byte[] data = out.toByteArray();
		try
		{
			if(out!=null)
				out.close();
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
		return data;
	}
}