package cn.edu.jxau;

import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.JComboBox;
import javax.swing.JApplet;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JFrame;

import java.awt.GridLayout;
import java.awt.BorderLayout;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Connection;

public class JDBCTest extends JApplet
{
	private JComboBox<String> user;
	private JButton entry;
	private JButton modifyPassword;
	private JPasswordField password;
	private String[] userName = {"super","study","live","discipline"};
	public JDBCTest()
	{
		user = new JComboBox<String>(userName);
		entry = new JButton("Entry");
		modifyPassword = new JButton("Modify password");
		password = new JPasswordField(10);
		setUI();
		addListener();
	}
	public void setUI()
	{
		JPanel p1 = new JPanel(new GridLayout(2,2,0,0));
		p1.add(new JLabel("User"));
		p1.add(user);
		p1.add(new JLabel("Password"));
		p1.add(password);
		
		JPanel p2 = new JPanel();
		p2.add(entry);
		p2.add(modifyPassword);
		this.add(p1,BorderLayout.CENTER);
		this.add(p2,BorderLayout.SOUTH);
	}
	public void addListener()
	{
		password.addActionListener(
				                      new ActionListener()
				                      {
				                    	  public void actionPerformed(ActionEvent e)
				                    	  {
				                    		  String p=String.copyValueOf(password.getPassword()); //get password
				                    		  String u=(String)user.getSelectedItem();//get userName
				                    		  try
				                    		  {
				                    			  System.out.println("62---"+u+"  "+p);
				                    			  entryDB(u,p);
				                    		  }
				                    		  catch(SQLException ex)
				                    		  {
				                    			  JOptionPane.showMessageDialog(null,"Password error");
				                    		  }
				                    		  catch(ClassNotFoundException ex)
				                    		  {
				                    			  System.out.println("74---"+ex);
				                    		  }				                    		 
				                    	  }
				                      }
				                  );
		entry.addActionListener(
				                   new ActionListener()
				                   {
				                	   public void actionPerformed(ActionEvent e)
				                	   {
				                		   String p = String.copyValueOf(password.getPassword());
				                		   String u = (String)user.getSelectedItem();
				                		   try
				                    	   {
				                			    
				                    		   entryDB(u,p);
				                    	   }
				                    	   catch(SQLException ex)
				                    	   {
				                    		   JOptionPane.showMessageDialog(null,"Password error");
				                    	   }
				                    	   catch(ClassNotFoundException ex)
				                    	   {
				                    		   System.out.println("97---"+ex);
				                    	   }	
				                	   }
				                   }
				               );
		modifyPassword.addActionListener(
				                            new ActionListener()
				                            {
				                            	public void actionPerformed(ActionEvent e)
				                            	{
				                            		System.out.println();
				                            		JFrame temp = new ModifyPassword((String)user.getSelectedItem());
				                            		temp.setVisible(true);
				                            		temp.setSize(300,400);
				                            		temp.setLocationRelativeTo(null);
				            				    }
				                            }
				                        );
	}
	public void entryDB(String user,String password)//connect database
	                                                  throws SQLException,ClassNotFoundException
	{
		Connection connection = JDBCUtil.getConnection(user, password);
		JDBCUtil.free(connection,null,null);
	}
}