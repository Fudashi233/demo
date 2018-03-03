package cn.edu.jxau;

import javax.swing.JApplet;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel ;
import javax.swing.JScrollPane;

import javax.swing.table.TableRowSorter;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;


//利用id DELETE是最大的BUG   

import java.awt.BorderLayout;
import java.awt.GridLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class JDBCTest extends JApplet
{
	private JTextField databaseName;
	private JTextField tableName;
	private JTextField sql;
	private JTable table;
	private MyTableModel model;
	private JCheckBox rowSelected;
	private JCheckBox columnSelected;
	private JButton addColumn;
	private JButton addRow;
	private JButton removeColumn;
	private JButton removeRow;
	private Object[][] data;
	private Object[] columnNames;
	public JDBCTest()
	{
		databaseName = new JTextField("test",10);
		tableName = new JTextField("dormitory",10);
		sql = new JTextField(20);
		model = new MyTableModel();
		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		rowSelected = new JCheckBox("Row selected",true);
		columnSelected = new JCheckBox("Column selected",false);
		addColumn = new JButton("Add column");
		addRow = new JButton("Add Row");
		removeColumn = new JButton("Remove column");
		removeRow = new JButton("Remove row");
		data = null;
		columnNames =null;
		setUI();
		addListener();
	}
	
	public void setUI()
	{
		JPanel p1 = new JPanel();
		p1.add(new JLabel("Database Name"));
		p1.add(databaseName);
		p1.add(new JLabel("Table Name"));
		p1.add(tableName);
		this.add(p1,BorderLayout.NORTH);
		
		this.add(new JScrollPane(table),BorderLayout.CENTER);
		
		JPanel p2 = new JPanel(new GridLayout(3,2,0,0));
		p2.add(rowSelected);
		p2.add(columnSelected);	
		p2.add(addRow);
		p2.add(addColumn);		
		p2.add(removeRow);
		p2.add(removeColumn);
		
		JPanel p3 = new JPanel(new GridLayout(2,1,0,0));
		p3.add(p2);
		//p3.add(new JScrollPane(sql));  //不支持
		p3.add(sql);
		this.add(p3,BorderLayout.SOUTH);
	}
	
	public void addListener()
	{
		tableName.addActionListener(
				                        new ActionListener()
				                        {
				                        	public void actionPerformed(ActionEvent e)
				                        	{
				                        		String dName = databaseName.getText();
				                        		String tName = tableName.getText();
				                        		if(dName.equals("")||tName.equals(""))
				                        		{
				                        			JOptionPane.showMessageDialog(null,"Please Input complete database/table name");
				                        			return ;
				                        		}
				                        		try
				                        		{
				                        			data = getData(dName,tName);
				                        			columnNames = getColumnNames(dName,tName);
				                        			model = new MyTableModel(data,columnNames);
				                        			TableRowSorter<TableModel> sorter = new TableRowSorter(model);
				                        			table.setRowSorter(sorter);
				                        			table.setModel(model);
				                        		}
				                        		catch(SQLException ex)
				                        		{
				                        			System.out.println(ex);
				                        		}
				                        	
				                        	}
				                        }
				                   );
		rowSelected.addActionListener(
				                          new ActionListener()
				                          {
				                        	  public void actionPerformed(ActionEvent e)
				                        	  {
				                        		  table.setRowSelectionAllowed(rowSelected.isSelected());
				                        	  }
				                          }
				                     );
		columnSelected.addActionListener(
				                             new ActionListener()
				                             {
				                            	 public void actionPerformed(ActionEvent e)
				                            	 {
				                            		 table.setColumnSelectionAllowed(columnSelected.isSelected());
				                            	 }
				                             }			          
				                        );
		addRow.addActionListener(
				                     new ActionListener()
				                     {
				                    	 public void actionPerformed(ActionEvent e)
				                    	 {
				                    		 if(table.getSelectedRow()<0)
				                    			 model.addRow(new java.util.Vector());
				                    		 else
				                    		     model.insertRow(table.getSelectedRow(),new java.util.Vector());
				                    	 }
				                     }
				                );
		addColumn.addActionListener(
				                         new ActionListener()
				                         {
				                        	 public void actionPerformed(ActionEvent e)
				                        	 {
				                        	     String columnName = JOptionPane.showInputDialog(null,"Input column name"); 
				                        		 if(columnName == null)
				                        			 return ;
				                        		 else if(!columnName.equals(""))
				                        		 {
				                        			 model.addColumn(columnName);
				                        		 }
				                        	 }
				                         }
				                   );
		removeRow.addActionListener(
				                        new ActionListener()
				                        {
				                        	public void actionPerformed(ActionEvent e)
				                        	{
				                        		model.removeRow(table.getSelectedRow());
				                        	}
				                        }
				                   );
		removeColumn.addActionListener(
				                           new ActionListener()
				                           {
				                        	   public void actionPerformed(ActionEvent e)
				                        	   {
				                        		   DefaultTableColumnModel model = (DefaultTableColumnModel)table.getColumnModel();
				                        		   TableColumn column = model.getColumn(table.getSelectedColumn());
				                        		   model.removeColumn(column);
				                        	   }
				                           }
				                      );
		sql.addActionListener(
				                  new ActionListener()
				                  {
				                	  public void actionPerformed(ActionEvent e)
				                	  {
				                		  String tempSql = sql.getText();
				                		  try
				                		  {
				                			  operateDatabase(databaseName.getText(),tempSql);
				                			  process(tableName.getActionListeners(),new ActionEvent(tableName,ActionEvent.ACTION_PERFORMED,"sql"));
				                		  }
				                		  catch(SQLException ex)
				                		  {
				                			  System.out.println(ex);
				                		  }
				                		  
				                	  }
				                  }
				             );
		table.addMouseListener(
				                   new MouseAdapter()
				                   {
				                	   public void mouseClicked(MouseEvent e)
				                	   {
				                		   if(e.getClickCount()==2)
				                		   {
				                			   String temp = JOptionPane.showInputDialog(null,"Modify your value");
				                			   if(temp==null)
				                				   return ;
				                			   else if(!temp.equals(""))
				                			   {
				                				   try
				                				   {
				                					   modify(databaseName.getText(),tableName.getText(),temp);
				                					   process(tableName.getActionListeners(),new ActionEvent(tableName,ActionEvent.ACTION_PERFORMED,"modify"));
				                				   }
				                				   catch(SQLException ex)
				                				   {
				                					   System.out.println(ex);
				                				   }
				                			   }
				                		   }
				                	   }
				                   }
				              );
	}
	public void modify(String databaseName,String tableName,String temp) throws SQLException
	{
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try
		{
		    String user = "root";
		    String password = "4869";
		    connection = JDBCUtil.getConnection(databaseName, user, password);
		    statement = connection.createStatement();
		    String sql = "UPDATE "+tableName+" SET "+this.columnNames[table.getSelectedColumn()]+" = "+temp+" WHERE id = "+(table.getSelectedRow()+1);
		    System.out.println("247---"+sql);
		    statement.executeUpdate(sql);
		}
		finally
		{
			JDBCUtil.free(connection,statement,resultSet);
		}
	}
	public Object[][] getData(String databaseName,String tableName) throws SQLException
	{
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		String user = "root";
		String password = "4869";
		String sql= "SELECT * FROM "+tableName;
		Object[][] data = null;
		try
		{
			connection = JDBCUtil.getConnection(databaseName, user, password);
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sql);
			ResultSetMetaData metaData = resultSet.getMetaData();
			int column = metaData.getColumnCount();
			int row = this.getRowCount(resultSet);
			data= new Object[row][column];
			int i = 0;
			while(resultSet.next())
			{
				for(int j=0;j<column;j++)
					data[i][j] = resultSet.getObject(j+1);
				i++;
			}
		}
		finally
		{
			JDBCUtil.free(connection,statement,resultSet);
		}
		return data;
	}
	public int getRowCount(ResultSet resultSet) throws SQLException
	{
		int rowCount = 0;
		while(resultSet.next())
			rowCount++;
		resultSet.beforeFirst();
		return rowCount;
	}
	
	public Object[] getColumnNames(String databaseName,String tableName) throws SQLException
	{
		String user = "root";
		String password = "4869";
		String sql= "SELECT * FROM "+tableName;
		Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        Object[] columnNames = null;
        try
        {
        	connection = JDBCUtil.getConnection(databaseName, user, password);
    		statement = connection.createStatement();
    		resultSet = statement.executeQuery(sql);
    		ResultSetMetaData metaData = resultSet.getMetaData();
    		int column = metaData.getColumnCount();
    		columnNames = new Object[column];
    		for(int i=0;i<column;i++)
    			columnNames[i] = metaData.getColumnName(i+1); 
        }
        finally
        {
        	JDBCUtil.free(connection,statement,resultSet);
        }
		return columnNames;
	}
	public void operateDatabase(String databaseName,String sql) throws SQLException
	{
		String user = "root";
		String password = "4869";
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try
		{
			connection = JDBCUtil.getConnection(databaseName, user, password);
			statement = connection.createStatement();
			statement.execute(sql);
		}
		finally
		{
			JDBCUtil.free(connection,statement,resultSet);
		}
	}
	public void process(ActionListener[] listenerArray,ActionEvent e)
	{
		for(int i=0;i<listenerArray.length;i++)
			listenerArray[i].actionPerformed(e);
	}
}