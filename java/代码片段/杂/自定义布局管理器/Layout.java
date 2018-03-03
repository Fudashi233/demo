import java.awt.LayoutManager;
import java.io.Serializable;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;

public class Layout implements LayoutManager,Serializable
{
	public void addLayoutComponent(String name,Component comp)
	{
		
	}
	
	public void removeLayoutComponent(Component comp)
	{
			
	}
	
	public void layoutContainer(Container parent)
	{
		int number=parent.getComponentCount();
		int x=10;
		int y=10;
		for(int i=0;i<number;i++)
		{
			Component c=parent.getComponent(i);
            Dimension d=c.getPreferredSize();
		    c.setBounds(x,y,d.width,d.height);
			x+=d.width;
			y+=d.height;
		}
	}
	
	public Dimension minimumLayoutSize(Container parent)
	{
		return new Dimension(0,0);
	}
	
	public Dimension preferredLayoutSize(Container parent)
	{
		return minimumLayoutSize(parent);
	}
	
	public Layout()
	{
		
	}
}