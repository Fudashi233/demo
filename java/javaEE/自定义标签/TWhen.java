package cn.edu.jxau.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class TWhen extends SimpleTagSupport
{
	private boolean test;
	public void setTest(boolean test)
	{
		this.test = test;
	}
	public void doTag() throws IOException,JspException
	{
		TChoose tag = (TChoose)getParent();
		if(!tag.isExecute())
			if(test)
			{
				tag.setExecute(true);
				this.getJspBody().invoke(null);
			}
	}
}
