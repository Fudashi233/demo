package cn.edu.jxau.tag;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class TToUpperCase extends SimpleTagSupport
{
	@Override
	public void doTag() throws IOException, JspException
	{
		JspFragment body = getJspBody();
		if(body==null)
			return;
		StringWriter writer = new StringWriter();
		body.invoke(writer);
		String bodyStr = writer.getBuffer().toString();
		this.getJspContext().getOut().println(bodyStr.toUpperCase());
	}
}
