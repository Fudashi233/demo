import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.Graphics;


public class Panel extends JPanel
{
	private JButton jbtResume;
	private JButton jbtSuspend;
	private JSlider slider;
	private Ball MyBall;
	public Panel()
	{
		jbtResume=new JButton("Resume");
		jbtSuspend=new JButton("Suspend");
		JPanel p1=new JPanel();
		p1.add(jbtResume);
		p1.add(jbtSuspend);
		
		slider=new JSlider(0,500);
		slider.setValue(250);
		slider.setOrientation(SwingConstants.HORIZONTAL);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setMajorTickSpacing(50);
		
		MyBall=new Ball();
		
		this.setLayout(new BorderLayout(5,5));
		this.add(p1,BorderLayout.SOUTH);
		this.add(MyBall,BorderLayout.CENTER);
		this.add(slider,BorderLayout.NORTH);
		//fit listener;
		jbtResume.addActionListener(
		                                new ActionListener()
										{
											public void actionPerformed(ActionEvent e)
											{
												MyBall.resume();
											}
										}
		                            );
	    jbtSuspend.addActionListener(
		                                new ActionListener()
										{
											public void actionPerformed(ActionEvent e)
											{
												MyBall.suspend();
											}
										}
  									);
		slider.addChangeListener(
		                            new ChangeListener()
									{
										public void stateChanged(ChangeEvent e)
										{
											int value=slider.getValue();
											MyBall.setDelay(value);
										}
									}
		                        );
	}
}