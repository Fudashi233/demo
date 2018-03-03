package cn.edu.jxau.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class TOtherwise extends SimpleTagSupport
{
	public void doTag() throws JspException, IOException
	{
		TChoose tag = (TChoose)this.getParent();
		if(!tag.isExecute())
		{
			this.getJspBody().invoke(null);
		}
	}
}
