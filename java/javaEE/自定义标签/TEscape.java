package cn.edu.jxau.tag;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class TEscape extends SimpleTagSupport
{
	private String filter(String content)
	{
		StringBuilder builder = new StringBuilder(content.length()+512);
		for(int i=0,len=content.length();i<len;i++)
		{
			char tempCh = content.charAt(i);
			if(tempCh == '<')
			{
				builder.append("&lt;");
			}
			else if(tempCh == '>')
			{
				builder.append("&gt;");
			}
			else if(tempCh == '&')
			{
				builder.append("&amp;");
			}
			else if(tempCh== '"')
			{
				builder.append("&quot;");
			}
			else 
			{
				builder.append(tempCh);
			}
		}
		return builder.toString();
	}
	@Override
	public void doTag() throws IOException,JspException
	{
		StringWriter writer = new StringWriter();
		this.getJspBody().invoke(writer);
		this.getJspContext().getOut().println(filter(writer.getBuffer().toString()));
	}
}
