package cn.edu.jxau.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class TChoose extends SimpleTagSupport
{
	private boolean isExecute;
	public void setExecute(boolean isExecute)
	{
		this.isExecute = isExecute;
	}
	public boolean isExecute()
	{
		return this.isExecute;
	}
	@Override
	public void doTag() throws IOException,JspException
	{
		this.getJspBody().invoke(null);
	}
}
