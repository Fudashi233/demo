package cn.edu.jxau.tag;

import javax.servlet.jsp.tagext.TagSupport;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.ArrayList;
import java.util.Iterator;

@SuppressWarnings("serial")
public class ForEachTagX<T> extends TagSupport
{
	private String var;
	private Collection<T> collection;
	private Iterator<T> iterator;
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
	public void setVar(String var)
	{
		this.var = var;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setItems(Object items)
	{
		if(items instanceof Collection)
		{
			collection = (Collection)items;
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
				Object obj = Array.get(items,i);
				((ArrayList)collection).add(obj);
			}
		}
		iterator = collection.iterator();
	}
	public void saveItem(Object obj)
	{
		pageContext.setAttribute(var,obj);
	}
}
