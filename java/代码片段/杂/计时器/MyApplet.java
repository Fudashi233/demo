import javax.swing.JApplet;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.JFrame;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;
import java.awt.BorderLayout;

public class MyApplet extends JApplet
{
	private Clock myClock;
	public MyApplet()
	{
		myClock=new Clock();
		this.add(myClock,BorderLayout.CENTER);
	}
	public static void main(String[] args)
	{
		JFrame frame=new JFrame("Windows");
		MyApplet applet=new MyApplet();
		frame.add(applet);
		frame.setSize(300,400);
		//frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
class Clock extends JPanel
{
	private int time;
	private Timer timer;
	class TimerListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			time--;
			repaint();
		}
	}
	public Clock()
	{
		time=10;
		timer=new Timer(1000,new TimerListener());
		timer.start();
	}	
	public Clock(int t)
	{
		time=t;
		timer=new Timer(1000,new TimerListener());
		timer.start();
	}
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if(time==0)
			timer.stop();
		FontMetrics fm=g.getFontMetrics();
		Font f=new Font("Californian FB",Font.BOLD,50);
		g.setFont(f);
		String st=String.valueOf(time);
		g.drawString(st,this.getWidth()/2-fm.stringWidth(st)/2,this.getHeight()/2-fm.getAscent()/2);
	}
}