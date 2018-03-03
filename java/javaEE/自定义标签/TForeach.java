package cn.edu.jxau.tag;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class TForeach extends SimpleTagSupport
{
	private Object items;
	private String var;
	private Collection collection;
	@Override
	public void doTag() throws JspException, IOException
	{
		Iterator<?> it = collection.iterator();
		JspContext jspContext = this.getJspContext();
		while(it.hasNext())
		{
			Object obj = it.next();
			
			jspContext.setAttribute(var,obj,PageContext.PAGE_SCOPE);
			this.getJspBody().invoke(null);
		}
	}
	public void setVar(String var)
	{
		this.var = var;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setItems(Object items)
	{
		this.items = items;
		if(items instanceof Collection)
		{
			collection = (Collection<?>) items;
		}
		else if(items instanceof Map)
		{
			collection = ((Map) items).entrySet();
		}
		else if(items.getClass().isArray())
		{
			collection = new ArrayList();
			int len = Array.getLength(items);
			for(int i=0;i<len;i++)
			{
				collection.add(Array.get(items,i));
			}
		}
	}
}
