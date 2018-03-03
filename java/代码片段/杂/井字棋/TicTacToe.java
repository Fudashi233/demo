import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;


import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;
public class TicTacToe extends JPanel
{
	private char whoseTurn;
	private JLabel label;
	private Cell[][] cell=new Cell[3][3];
	public TicTacToe()
	{
		whoseTurn='X';// blank
		
		JPanel p1=new JPanel(new GridLayout(3,3,0,0));
		for(int i=0;i<3;i++)
			for(int j=0;j<3;j++)
			{
				cell[i][j]=new Cell();/////////delete it?
				cell[i][j].setBorder(new LineBorder(Color.BLACK,1));
				p1.add(cell[i][j]);
			}
		
		label=new JLabel("X turn to play");  //X start
		
		this.setLayout(new BorderLayout(0,0));
		this.add(p1,BorderLayout.CENTER);
		this.add(label,BorderLayout.SOUTH);
	} 
	public boolean isDraw()
	{
		for(int i=0;i<3;i++)
			for(int j=0;j<3;j++)
				if(cell[i][j].getToken()==' ')
					return false;
	    return true;
	}
	public boolean isWin(char token)
	{
		for(int i=0;i<3;i++)
			if(cell[i][0].getToken()==token&&
		       cell[i][1].getToken()==token&&
			   cell[i][2].getToken()==token)
			    return true;
	    for(int i=0;i<3;i++)
			if(cell[0][i].getToken()==token&&
		       cell[1][i].getToken()==token&&
			   cell[2][i].getToken()==token)
			    return true;
		if(cell[0][0].getToken()==token&&
		   cell[1][1].getToken()==token&&
		   cell[2][2].getToken()==token)
		        return true;
		if(cell[0][2].getToken()==token&&
		   cell[1][1].getToken()==token&&
		   cell[2][0].getToken()==token)
		        return true;
		return false;
	}
	public class Cell extends JPanel
	{
		private char token;
		public Cell()
		{
			token=' ';//blank
			this.addMouseListener(
			                        new MouseAdapter()
									{
										public void mouseClicked(MouseEvent e)
										{
 											if(token==' '&&whoseTurn!=' ')
											{
												setToken(whoseTurn);
											}
											/////////////////////
											if(isDraw())
											{
												label.setText("It is Draw");
												whoseTurn=' ';
											}
											else if(isWin(whoseTurn))
											{
												label.setText(whoseTurn+" is win");
												whoseTurn=' ';
											}
											else
											{
												whoseTurn=(whoseTurn=='X')?'O':'X';
												label.setText(whoseTurn+"'s turn to play");
											}
										}
									}
			                     );
		}
		public void setToken(char t)
		{
			token=t;
			repaint();
		}
		public char getToken()
		{
			return token;
		}
		protected void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			if(token=='O')
				g.drawOval(10,10,this.getWidth()-10,this.getHeight()-10);
			else if(token=='X')
			{
				g.drawLine(10,10,this.getWidth()-10,this.getHeight()-10);
				g.drawLine(10,this.getHeight()-10,this.getWidth()-10,10);
			}
			System.out.println("paint over");
		}
	}
}