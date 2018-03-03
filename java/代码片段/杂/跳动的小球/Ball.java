import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Ball extends JPanel

{
	private int ix=2;
	private int iy=4;
	private int xCoordinate;
	private int yCoordinate;
	private int radius;
	private Timer time;
	private int delay;
	public Ball()
	{
		xCoordinate=10;
                yCoordinate=10;
	        radius=25;
		delay=100;
	        time=new Timer(delay,new TimerListener());
		time.start();
	}
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if(xCoordinate<radius)
			ix=Math.abs(ix);
		if(xCoordinate>(this.getWidth()-radius))
                        ix=-Math.abs(ix);
                if(yCoordinate<radius)
			iy=Math.abs(iy);
		if(yCoordinate>(this.getHeight()-radius))
			iy=-Math.abs(iy);
		xCoordinate+=ix;
		yCoordinate+=iy;
		g.fillOval(xCoordinate,yCoordinate,2*radius,2*radius);
		System.out.println("paint over");
	}
	private class TimerListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			repaint();
		}
	}
	public void suspend()
	{
		time.stop();
	}
	public void resume()
	{
		time.start();
	}
	public int getDelay()
	{
		return delay;
	}
	public void setDelay(int d)
	{
		delay=d;
		time.setDelay(d);
	}
}