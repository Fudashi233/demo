package cn.edu.jxau;

import javax.swing.JFrame;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridLayout;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;

public class ModifyPassword extends JFrame
{
	private JPasswordField oldPassword;
	private JPasswordField newPassword;
	private JPasswordField confirmPassword;
	private JButton confirm;
	private JButton cancel;
	private String user;
    public ModifyPassword(String user)
    {
    	oldPassword = new JPasswordField(20);
    	newPassword = new JPasswordField(20);
   	    confirmPassword = new JPasswordField(20);
    	confirm = new JButton("Confirm");
   	    cancel = new JButton("Cancel");
   	    this.user = user;
   	    setUI();
   	    addListener();
    }
    public void setUI()
    {
    	this.setLayout(new GridLayout(4,2,0,0));
    	this.add(new JLabel("Old password"));
    	this.add(oldPassword);
    	this.add(new JLabel("New password"));
    	this.add(newPassword);
    	this.add(new JLabel("Confirm password"));
    	this.add(confirmPassword);
    	this.add(confirm);
    	this.add(cancel);
    }
    public void addListener()
    {
    	confirm.addActionListener(
    			                     new ActionListener()
    			                     {
    			                    	 public void actionPerformed(ActionEvent e)
    			                    	 {
    			                    		 String oldP = String.copyValueOf(oldPassword.getPassword());
    			                    		 String newP = String.copyValueOf(newPassword.getPassword());
    			                    		 String conP = String.copyValueOf(confirmPassword.getPassword());
    			                    		 if(newP.equals(conP))
    			                    		 {
    			                    			 try
    			                    			 {
    			                    				 connectDB(oldP,newP);
    			                    			 }
    			                    			 catch(SQLException ex)
    			                    			 {
    			                    				 JOptionPane.showMessageDialog(null,"Old password error."); 
    			                    			 }
    			                    			 catch(ClassNotFoundException ex)
    			                    			 {
    			                    				 System.out.println("71---"+ex);
    			                    			 }
    			                    			 JOptionPane.showMessageDialog(null,"Password modify success");
    			                    		 }
    			                    		 else
    			                    		 {
    			                    			 JOptionPane.showMessageDialog(null,"Tow input are different");
    			                    		 }
    			                    	 }
    			                     }
    			                 );
    	cancel.addActionListener(
    			                     new ActionListener()
    			                     {
    			                    	 public void actionPerformed(ActionEvent e)
    			                    	 {
    			                    		 ModifyPassword.this.removeNotify();
    			                    	 }
    			                     }
    			                 );
    }
    public void connectDB(String oldPassword,String newPassword) throws SQLException,ClassNotFoundException
    {
    	String sql = "SET PASSWORD = PASSWORD("+"'"+newPassword+"'"+")";
    	Connection connection = JDBCUtil.getConnection(user,oldPassword);
    	Statement statement = connection.createStatement();
    	statement.executeUpdate(sql);
    	JDBCUtil.free(connection,statement,null);
    }
}
