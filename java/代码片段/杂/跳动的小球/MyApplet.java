import javax.swing.*;
public class MyApplet extends JApplet
{
	public MyApplet()
	{
		this.add(new Panel());
	}
	public static void main(String[] args)
	{
		JFrame frame=new JFrame("Windows");
		MyApplet applet=new MyApplet();
		frame.add(applet);
		frame.setSize(300,400);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
	}
}