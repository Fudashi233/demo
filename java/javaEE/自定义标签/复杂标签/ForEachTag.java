package cn.edu.jxau.tag;

import javax.servlet.jsp.tagext.TagSupport;

import java.util.Iterator;
import java.util.Collection;

public class ForEachTag extends TagSupport
{
	private String var;
	private Iterator<?> iterator;
	public void setVar(String var)
	{
		this.var = var;
	}
	public void setIterator(Collection<?> collection)
	{
		iterator = collection.iterator();
	}
	@Override
	public int doStartTag()
	{
		if(iterator.hasNext())
		{
			saveItem(iterator.next());
			return TagSupport.EVAL_BODY_INCLUDE;
		}
		else
		{
			return TagSupport.SKIP_BODY;
		}
	}
	@Override
	public int doAfterBody()
	{
		if(iterator.hasNext())
		{
			saveItem(iterator.next());
			return TagSupport.EVAL_BODY_AGAIN;
		}
		else
		{
			return TagSupport.SKIP_BODY;
		}
	}
	public void saveItem(Object obj)
	{
		pageContext.setAttribute(var,obj);
	}
}