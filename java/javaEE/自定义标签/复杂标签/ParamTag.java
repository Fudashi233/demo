package cn.edu.jxau.tag;

import javax.servlet.jsp.tagext.TagSupport;

import javax.servlet.http.HttpServletResponse;


public class ParamTag extends TagSupport
{
	private String name;
	private String value;
	public void setName(String name)
	{
		this.name = name;
	}
	public void setValue(String value)
	{
		this.value = value;
	}
	@Override
	public int doStartTag()
	{
		String page = ((ForwardTag) getParent()).getPage();
		HttpServletResponse response= (HttpServletResponse)pageContext.getResponse();
		if(page.indexOf("?")>=0)  //URL�д��ڡ�������˵����URL���Ѵ��ڲ���
		{
			page = response.encodeURL(page+"&"+name+"="+value);
		}
		else
		{
			page = response.encodeURL(page+"?"+name+"="+value);
		}
		((ForwardTag)getParent()).setPage(page);
		return SKIP_BODY;
	}
}
