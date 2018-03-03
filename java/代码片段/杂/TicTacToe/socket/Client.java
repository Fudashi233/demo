import javax.swing.JApplet;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.BorderFactory;

import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Color;

import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

import java.net.Socket;

import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import java.util.concurrent.TimeUnit;

public class Client extends JApplet implements Constants,Runnable
{
    private class Cell extends JPanel
    {
    	private int column;
    	private int row;
    	private char token;
    	public Cell(int row,int column)
    	{
    		this.column = column;
    		this.row = row;
    	    token = 0;
    	    addListener();
    	}
    	public void paintComponent(Graphics g)
    	{
    		super.paintComponent(g);
    		if(token=='X')
    		{
    			g.drawLine(20,20,getWidth()-20,getHeight()-20);
    			g.drawLine(20,getHeight()-20,getWidth()-20,20);
    		}
    		else if(token=='O')
    		{
    			g.drawOval(20,20,getWidth()-40,getHeight()-40);
    		}
    	}
    	private void addListener()
    	{
    		this.addMouseListener(
    				                  new MouseAdapter()
    				                  {
    				                	  @Override
    				                	  public void mouseClicked(MouseEvent e)
    				                	  {
    				                		  if(isMyTurn&&token==0)
    				                		  {
    				                			  setToken(myToken);
    				                			  selectedRow = row;
    				                			  selectedColumn = column;
    				                			  isWait = false;
    				                			  isMyTurn = false;
    				                			  title.setText("wait for the other player to move");
    				                		  }
    				                	  }
    				                  }
    				             );
    	}
    	public void setToken(char token)
    	{
    		this.token = token;
    		repaint();
    	}
    }
    private boolean isMyTurn;
    private boolean isWait;
    private boolean isContinue;
    private char myToken;
    private DataInputStream fromServer;
    private DataOutputStream toServer;
    private int selectedRow;
    private int selectedColumn;
    private Socket socket;
    private Cell[][] cells;
    private JLabel title;
    @Override
    public void init()
    {
    	cells = new Cell[3][3];
    	for(int i=0;i<3;i++)
    		for(int j=0;j<3;j++)
    			cells[i][j] = new Cell(i,j);
    	title = new JLabel();
    	title.setHorizontalAlignment(JLabel.CENTER);
    	isContinue = true;
    	isWait = true;
    	setUI();
    	try
    	{
    		socket = new Socket(getCodeBase().getHost(),8000);
    		toServer = new DataOutputStream(socket.getOutputStream());
    		fromServer = new DataInputStream(socket.getInputStream());
    	}
    	catch(IOException ex)
    	{
    		ex.printStackTrace();
    	}
    	setUI();
    	new Thread(this).start();
    }
    private void setUI()
    {
    	 JPanel p1 = new JPanel(new GridLayout(3,3,0,0));
    	 for(int i=0;i<3;i++)
    		 for(int j=0;j<3;j++)
    		 {
    			 cells[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
    			 p1.add(cells[i][j]);
    		 }
    	 
         this.add(title,BorderLayout.NORTH);
         this.add(p1,BorderLayout.CENTER);
    }
    public void sendInfo()
    {
    	try
    	{
    		toServer.writeInt(selectedRow);
    	    toServer.writeInt(selectedColumn);
    	}
    	catch(IOException ex)
    	{
    		ex.printStackTrace();
    	}
    	
    }
    public void receiveInfo()
    {
    	int status = 0;
    	try
    	{
    		status = fromServer.readInt();
    	    if(status == P1_WIN)
    	    {
    		    title.setText("P1 win !!!");
    		    move();
    		    isContinue = false;
    	    }
    	    else if(status == P2_WIN)
    	    {
    		    title.setText("P2 win !!!");
    		    move();
    		    isContinue = false;
    	    }
    	    else if(status == DRAW)
    	    {
    		    title.setText("Draw !!!");
    		    move();
    		    isContinue = false;
    	    }
    	    else if(status == CONTINUE)
    	    {
    		    move();
    		    title.setText("My turn");
    		    isMyTurn = true;
    	    }
    	}
    	catch(IOException ex)
    	{
    		ex.printStackTrace();
    	}
    }
    private void move()
    {
    	try
    	{
    		 int row = fromServer.readInt();
		     int column = fromServer.readInt();
		     cells[row][column].setToken(myToken=='O'?'X':'O');
    	}
    	catch(IOException ex)
    	{
    		ex.printStackTrace();
    	}
    	
    }
    public void waitForAction()
    {
    	try
    	{
    		while(isWait)
    	    {
    		    TimeUnit.MILLISECONDS.sleep(100);
    	    }
    	}
    	catch(InterruptedException ex)
    	{
    		ex.printStackTrace();
    	}
    	isWait = true;
    }
    @Override
    public void run()
    {
    	try
    	{
    		int player = fromServer.readInt();
    		if(player==P1)
    		{
    			myToken = 'X';
    			title.setText("Wait for p2 join the game");
    			fromServer.readInt();
    			isMyTurn = true;
    			title.setText("My turn");
    		}
    		else if(player==P2)
    		{
    			title.setText("wait for the other player to move");
    			isMyTurn = false;
    			myToken = 'O';
    		}
    		while(isContinue)
    		{
    			if(player==P1)
    			{
    				waitForAction();
    				sendInfo();
    				receiveInfo();
    			}
    			else if(player==P2)
    			{
    				receiveInfo();
    				waitForAction();
    				sendInfo();
    			}
    		}
    	}
    	catch(IOException ex)
    	{
    		ex.printStackTrace();
    	}
    }
}
