import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.BorderFactory;

import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Chessboard extends JFrame
{
	public static void main(String[] args)
	{
		new Chessboard();
	}
	private Cell[][] cells;
	private char token;
	private JLabel title;
	public Chessboard()
	{
		super("Chessboard");
		cells = new Cell[3][3];
		for(int i=0;i<3;i++)
			for(int j=0;j<3;j++)
			{
				cells[i][j] = new Cell(i,j);
				cells[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
			}
		token = 'X';
		
		title = new JLabel("It's your turn,p1");
		title.setHorizontalAlignment(JLabel.CENTER);
		
		setUI();
		addListener();
	}
	private void setUI()
	{
		JPanel p1 = new JPanel(new GridLayout(3,3,0,0));
	    for(int i=0;i<3;i++)
	    	for(int j=0;j<3;j++)
	    		p1.add(cells[i][j]);
	    
	    this.add(title,BorderLayout.NORTH);
	    this.add(p1,BorderLayout.CENTER);
	    this.setVisible(true);
	    this.pack();
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setLocationRelativeTo(null);
	}
	private void addListener()
	{
		
	}
	public char getThisTurn()
	{
		return this.token;
	}
	public void exchange()
	{
		token = token=='X'?'O':'X';
		if(token=='X')
		{
			title.setText("It's your turn,p1");
		}
		else
		{
			title.setText("It's your turn,p2");
		}
	}
	private Status judge()
	{
		for(int i=0;i<3;i++)
		{
			if(cells[i][0].getToken()==cells[i][1].getToken()&&
			      cells[i][0].getToken()==cells[i][2].getToken()&&
			          cells[i][1].getToken()==cells[i][2].getToken())
			{
				if(cells[i][0].getToken() =='X')
					return Status.P1_WON;
				else if(cells[i][0].getToken() == 'O')
					return Status.P2_WON;
			}
		}
		for(int i=0;i<3;i++)
		{
			if(cells[0][i].getToken()==cells[1][i].getToken()&&
			      cells[0][i].getToken()==cells[2][i].getToken()&&
			          cells[1][i].getToken()==cells[2][i].getToken())
			{
				if(cells[0][i].getToken() =='X')
					return Status.P1_WON;
				else if(cells[0][i].getToken() == 'O')
					return Status.P2_WON;
			}
		}
		if(cells[0][0].getToken()==cells[1][1].getToken()&&
				cells[0][0].getToken()==cells[2][2].getToken()&&
				    cells[1][1].getToken()==cells[2][2].getToken())
		{
			if(cells[0][0].getToken() =='X')
				return Status.P1_WON;
			else if(cells[0][0].getToken() == 'O')
				return Status.P2_WON;
		}
		if(cells[0][2].getToken()==cells[1][1].getToken()&&
				cells[2][0].getToken()==cells[1][1].getToken()&&
				    cells[0][2].getToken()==cells[2][0].getToken())
		{
			if(cells[0][2].getToken() =='X')
				return Status.P1_WON;
			else if(cells[0][2].getToken() == 'O')
				return Status.P2_WON;
		}
		for(int i=0;i<3;i++)
			for(int j=0;j<3;j++)
				if(cells[i][j].getToken()==' ')
					return Status.CONTINUE;
		
		return Status.DRAW;
		
	}
	private class Cell extends JPanel
	{
		private char token;
		private int column;
		private int row;
		public Cell(int row,int column)
		{
			this.row = row;
			this.column = column;
			token = ' ';
			this.addListener();
		}
		@Override
		public Dimension getPreferredSize()
		{
			return new Dimension(150,100);
		}
		@Override
		protected void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			if(token=='X')
			{
				g.drawLine(20,20,this.getWidth()-20,this.getHeight()-20);
				g.drawLine(20,this.getHeight()-20,this.getWidth()-20,20);
			}
			else if(token=='O')
			{
				g.drawOval(20,20,this.getWidth()-40,this.getHeight()-40);
			}
		}
		public char getToken()
		{
			return this.token;
		}
	    private void addListener()
	    {
	    	this.addMouseListener(
	    			                 new MouseAdapter()
	    			                 {
	    			                	 public void mouseClicked(MouseEvent e)
	    			                	 {
	    			                		 if(judge()==Status.CONTINUE&&Cell.this.token==' ')
	    			                		 {
	    			                			 
	    			                			 Cell.this.token = Chessboard.this.token;
	    			                		     Chessboard.this.exchange();
	    			                		     repaint();
	    			                		     switch(judge())
	    			                			 {
	    			                			     case DRAW:title.setText("Draw");break;
	    			                			     case P1_WON:title.setText("P1 won!!!");break;
	    			                			     case P2_WON:title.setText("P2 won!!!");break;
	    			                			     case CONTINUE:break;
	    			                			 }
	    			                		 }
	    			                		
	    			                	 }
	    			                 }
	    			             );
	    }
	}
}
