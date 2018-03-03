import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JFrame;

import java.awt.BorderLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.net.Socket;
import java.net.ServerSocket;

import java.util.Date;

import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;


public class Server extends JFrame implements Constants
{
	public static void main(String[] args)
	{
		new Server();
	}
    private JTextArea log;
    private JButton stop;
    private ServerSocket serverSocket;
    private static int session = 1;
    public Server()
    {
    	log = new JTextArea(30,40);
    	log.setEditable(false);
    	log.setLineWrap(true);
    	stop = new JButton("stop");
    	setUI();
    	addListener();
    	
    	try
    	{
    		serverSocket = new ServerSocket(8000);
    		while(true)
    		{
    			Socket p1 = serverSocket.accept();
    			log.append("----------------------"+session+"---------------------\n");
    			log.append(new Date()+" player1 join\n");
    			new DataOutputStream(p1.getOutputStream()).writeInt(P1);
    			
    			
    			Socket p2 = serverSocket.accept();
    			log.append(new Date()+" player2 join\n");
    			new DataOutputStream(p2.getOutputStream()).writeInt(P2);
    			session++;
    			new Thread(new HandlePlay(p1,p2)).start();
    		}
    	}
    	catch(IOException ex)
    	{
    		ex.printStackTrace();
    	}
    } 
    private void setUI()
    {
    	this.add(new JScrollPane(log),BorderLayout.CENTER);
    	this.add(stop,BorderLayout.SOUTH);
    	this.setVisible(true);
    	this.pack();
    	this.setLocationRelativeTo(null);
    	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    private void addListener()
    {
    	stop.addActionListener(
    			                  new ActionListener()
    			                  {
    			                	  public void actionPerformed(ActionEvent e)
    			                	  {
    			                		  System.exit(0);
    			                	  }
    			                  }
    			              );
    }
}

class HandlePlay implements Runnable,Constants
{
	private DataInputStream fromP1;
	private DataOutputStream toP1;
	private DataInputStream fromP2;
	private DataOutputStream toP2;
	private int row;
	private int column;
	private char[][] cells;
	public HandlePlay(Socket p1,Socket p2)
	{
		cells = new char[3][3];
		try
		{
			fromP1 = new DataInputStream(p1.getInputStream());
			toP1 = new DataOutputStream(p1.getOutputStream());
			fromP2 = new DataInputStream(p2.getInputStream());
			toP2 = new DataOutputStream(p2.getOutputStream());
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}
	@Override
	public void run()
	{
        try
        {
        	toP1.writeInt(P1);
        	while(true)
    		{
    			receiveInfo(fromP1);
    			cells[row][column] = 'X';
    			if(isWin('X'))
    			{
    				toP1.writeInt(P1_WIN);
    				toP2.writeInt(P1_WIN);
    				sendInfo(toP2);
    				break;
    			}
    			else if(isDraw())
    			{
    				toP1.writeInt(DRAW);
    				toP2.writeInt(DRAW);
    				sendInfo(toP2);
    				break;
    			}
    			else
    			{
    				toP2.writeInt(CONTINUE);
    				sendInfo(toP2);
    			}
    			
    			//p2
    			receiveInfo(fromP2);
    			cells[row][column] = 'O';
    			if(isWin('O'))
    			{
    				toP1.writeInt(P2_WIN);
    				toP2.writeInt(P2_WIN);
    				sendInfo(toP1);
    				break;
    			}
    			else if(isDraw())
    			{
    				toP1.writeInt(DRAW);
    				toP2.writeInt(DRAW);
    				sendInfo(toP1);
    				break;
    			}
    			else
    			{
    				toP1.writeInt(CONTINUE);
    				sendInfo(toP1);
    			}
    		}
        }
        catch(IOException ex)
        {
        	ex.printStackTrace();
        }
	}
	private void receiveInfo(DataInputStream from)
	{
		try
		{
			row = from.readInt();
			column = from.readInt();
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
		
	}
	private void sendInfo(DataOutputStream to)
	{
		try
		{
			to.writeInt(row);
			to.writeInt(column);
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}
	private boolean isWin(char token)
	{
		for(int i=0;i<3;i++)
			if(cells[i][0]==token&&
			    cells[i][1]==token&&
			        cells[i][2]==token)
		    return true;
		for(int i=0;i<3;i++)
			if(cells[0][i]==token&&
			    cells[1][i]==token&&
			        cells[2][i]==token)
            return true;
		if(cells[0][0]==token&&
		    cells[1][1]==token&&
		        cells[2][2]==token)
			return true;
		if(cells[0][2]==token&&
		    cells[1][1]==token&&
		        cells[2][0]==token)
			return true;
		return false;
	}
	private boolean isDraw()
	{
		for(int i=0;i<3;i++)
			for(int j=0;j<3;j++)
				if(cells[i][j]==0)
					return false;
		return true;
	}
}
