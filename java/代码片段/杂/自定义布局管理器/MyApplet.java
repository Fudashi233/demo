import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

import javax.swing.border.TitledBorder;
import javax.swing.JButton;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JApplet;

public class MyApplet extends JApplet
{
	private JRadioButton flowLayout;
	private JRadioButton gridLayout;
	private JRadioButton layout;
	public MyApplet()
	{
		flowLayout=new JRadioButton("Flow Layout");
		gridLayout=new JRadioButton("Grid Layout");
		layout=new JRadioButton("Layout");
		ButtonGroup group=new ButtonGroup();
		group.add(flowLayout);
		group.add(gridLayout);
		group.add(layout);
		JPanel p1=new JPanel();
		p1.add(flowLayout);
		p1.add(gridLayout);
		p1.add(layout);
		p1.setBorder(new TitledBorder("Select a layout manager"));
		
		JPanel p2=new JPanel(new Layout());
		p2.add(new JButton("Button 1"));
		p2.add(new JButton("Button 2"));
		p2.add(new JButton("Button 3"));
		p2.add(new JButton("Button 4"));
		
		this.add(p2,BorderLayout.CENTER);
		this.add(p1,BorderLayout.SOUTH);
		//fit listener
		flowLayout.addItemListener(
		                                new ItemListener()
										{
											public void itemStateChanged(ItemEvent e)
											{
												p2.setLayout(new FlowLayout());
												p2.revalidate();
											}
										}
		                          );
	    gridLayout.addItemListener(
		                                new ItemListener()
										{
											public void itemStateChanged(ItemEvent e)
											{
												p2.setLayout(new GridLayout(2,2,5,5));
												p2.revalidate();
											}
										}
		                          );
        layout.addItemListener(
		                            new ItemListener()
									{
										public void itemStateChanged(ItemEvent e)
										{
											p2.setLayout(new Layout());
											p2.revalidate();
										}
									}
		                      );
		
	}
}