package cn.edu.jxau;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTable;
import javax.swing.BorderFactory;
import javax.swing.JApplet;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.JLabel;
import javax.swing.ListSelectionModel;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;

import javax.swing.event.TableColumnModelListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSetMetaData;
public class JDBCTest extends JApplet
{
	private JTextField databaseName;
	private JTextField tableName;
	private JTable table;
	private JTextArea text;
	private JComboBox<Object> resizeMode;
	private JComboBox<Object> selectMode;
	private JCheckBox isShowGrid;
	private JSpinner rowHeight;
	private JSpinner rowMargin;
	private JCheckBox rowSelected;
	private JCheckBox columnSelected;
	private JButton addRow;
	private JButton removeRow;
	private JButton addColumn;
	private JButton removeColumn;
	public JDBCTest()
	{
		databaseName = new JTextField("test",10);
		tableName = new JTextField("student",10);
		table = new JTable();
		text = new JTextArea(10,0);
		Object[] resizeTitle={"AUTO_RESIZE_OFF","AUTO_RESIZE_LAST_COLUMN","AUTO_RESIZE_SUBSEQUENT_COLUMNS","AUTO_RESIZE_NEXT_COLUMN","AUTO_RESIZE_ALL_COLUMNS"};
		resizeMode = new JComboBox<Object>(resizeTitle);
		Object[] selectTitle={"MULTIPLE_INTERVAL_SELECTION","SINGLE_INTERVAL_SELECTION","SINGLE_SELECTION"};
		selectMode = new JComboBox<Object>(selectTitle);
		isShowGrid = new JCheckBox("Is show Grid?",true);
		rowHeight = new JSpinner(new SpinnerNumberModel(16,1,50,1));
		rowMargin = new JSpinner(new SpinnerNumberModel(1,1,15,1));
		rowSelected = new JCheckBox("Row selected",true);
		columnSelected = new JCheckBox("Column selected",false);
		addRow = new JButton("Add row");
		removeRow = new JButton("Remove row");
		addColumn = new JButton("Add column");
		removeColumn = new JButton("Remove column");
		this.setUI();
		this.setListener();
	}
	public void setUI()
	{
		JPanel p1 = new JPanel();
		p1.add(databaseName);
		p1.add(tableName);
		
		text.setLineWrap(true);
		text.setWrapStyleWord(true);
		text.setEditable(false);
		
		JPanel p2=new JPanel(new BorderLayout(0,0));
		p2.add(p1,BorderLayout.NORTH);
		p2.add(new JScrollPane(table),BorderLayout.CENTER);
		p2.add(new JScrollPane(text),BorderLayout.SOUTH);
		
		
		JPanel p4=new JPanel();
		p4.add(new JLabel("Row height"));
		p4.add(this.rowHeight);
		p4.add(new JLabel("Row margin"));
		p4.add(this.rowMargin);
		p4.add(this.isShowGrid);
		p4.setBorder(BorderFactory.createTitledBorder("Set apperance"));
		
		//p3.add(this.resizeMode,BorderLayout.NORTH);   不加resizeMode组合框
		//p3.add(this.selectMode,BorderLayout.NORTH);   不加selectMode组合框
		JPanel p5=new JPanel(); 
		p5.add(rowSelected);      
		p5.add(columnSelected);
		p5.setBorder(BorderFactory.createTitledBorder("Row/Column selected"));
		
		JPanel p6 = new JPanel(new GridLayout(2,0,0,0));
		p6.add(addRow);
		p6.add(addColumn);
		p6.add(removeRow);
		p6.add(removeColumn);
		p6.setBorder(BorderFactory.createTitledBorder("Add/Remove table"));
		
		JPanel p3=new JPanel(new GridLayout(3,0,0,0));
		p3.add(p5);
		p3.add(p6);
		p3.add(p4);
		
		this.add(p2,BorderLayout.CENTER);
		this.add(p3,BorderLayout.EAST);
	}
	public void setListener()
	{
		tableName.addActionListener(
				                       new ActionListener()
				                       {
				                    	   @Override
				                    	   public void actionPerformed(ActionEvent e)
				                    	   {
				                    		   String DName = databaseName.getText();//database name
				                    		   String TName = tableName.getText();//table name				             
                                               if(DName.length()!=0&&TName.length()!=0)
                                               {
                                            	   try
                                            	   {
                                            		   Object[][] data = JDBCTest.this.getTable(DName,TName);
                                                	   Object[] columnNames = JDBCTest.this.getColumnNames(DName,TName);
                                                	   DefaultTableModel model = new DefaultTableModel(data,columnNames);
                                                	   JDBCTest.this.table.setModel(model);
                                                	   TableRowSorter<TableModel> sorter= new TableRowSorter<TableModel>(model);
                                                	   JDBCTest.this.table.setRowSorter(sorter);
                                            	   }
                                            	   catch(SQLException ex)
                                            	   {
                                            		   System.out.println(ex);
                                            	   }
                                            	   catch(ClassNotFoundException ex)
                                            	   {
                                            		   System.out.println(ex);
                                            	   }
                                               }
                                               else
                                               {
                                            	   JOptionPane.showMessageDialog(null,"Please input complete information.");
                                               }
				                    	   }
				                       }
				                   );
		isShowGrid.addActionListener(
				                       new ActionListener()
				                       {
				                    	   public void actionPerformed(ActionEvent e)
				                    	   {
				                    		   table.setShowGrid(isShowGrid.isSelected());
				                    	   }
				                       }
				                   );
		rowHeight.addChangeListener(
				                       new ChangeListener()
				                       {
				                    	   public void stateChanged(ChangeEvent e)
				                    	   {
				                    		   int value=((Integer)rowHeight.getValue()).intValue();
				                    		   table.setRowHeight(value);
				                    	   }
				                       }
				                   );
		rowMargin.addChangeListener(
				                        new ChangeListener()
				                        {
				                        	public void stateChanged(ChangeEvent e)
				                        	{
				                        		int value=((Integer)rowMargin.getValue()).intValue();
				                        		table.setRowMargin(value);
				                        	}
				                        }
				                   );
	   resizeMode.addActionListener(
			                             new ActionListener()
			                             {
			                            	 public void actionPerformed(ActionEvent e)
			                            	 {
			                            		 String item=(String)resizeMode.getSelectedItem();
			                            		 if(item.equals("AUTO_RESIZE_OFF"))
			                            			 table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			                            		 else if(item.equals("AUTO_RESIZE_LAST_COLUMN"))
			                            			 table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
			                            		 else if(item.equals("AUTO_RESIZE_SUBSEQUENT_COLUMNS"))
			                            			 table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
			                            		 else if(item.equals("AUTO_RESIZE_ALL_COLUMNS"))
			                            			 table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
			                            		 else if(item.equals("AUTO_RESIZE_NEXT_COLUMN"))
			                            			 table.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
			                            		 else ;
			                            	 }
			                             }
			                        );
		selectMode.addActionListener(
				                          new ActionListener()
				                          {
				                        	  public void actionPerformed(ActionEvent e)
				                        	  {
				                        		  String item = (String)selectMode.getSelectedItem();
				                        		  if(item.equals("MULTIPLE_INTERVAL_SELECTION"))
				                        			  table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				                        		  else if(item.equals("SINGLE_INTERVAL_SELECTION"))
				                        			  table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
				                        		  else if(item.equals("SINGLE_SELECTION"))
				                        			  table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				                        		  else ;
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
			                        		{
			                        			System.out.println("249"+table.getSelectedRow());
			                        			DefaultTableModel model = (DefaultTableModel)table.getModel();
			                        			model.addRow(new java.util.Vector());
			                        		}
			                        		else
			                        		{
			                        			DefaultTableModel model = (DefaultTableModel)table.getModel();
			                        			model.insertRow(table.getSelectedRow(),new java.util.Vector());
			                        		}
			                        	}
			                        }
			                   );
	   addColumn.addActionListener(
			                           new ActionListener()
			                           {
			                        	   public void actionPerformed(ActionEvent e)
			                        	   {
			                        		   String columnName = JOptionPane.showInputDialog(null,"Input column name");
			                        		   if(columnName.isEmpty())
			                        			   return ;
			                        		   DefaultTableModel model = (DefaultTableModel)table.getModel();
			                        		   model.addColumn(columnName);
			                        	   }
			                           }
			                      );
	   removeRow.addActionListener(
			                           new ActionListener()
			                           {
			                        	   public void actionPerformed(ActionEvent e)
			                        	   {
			                        		   if(table.getSelectedRow()>=0)
			                        		   {
			                        			   DefaultTableModel model = (DefaultTableModel)table.getModel();
			                        			   model.removeRow(table.getSelectedRow());
			                        		   }
			                        	   }
			                           }
			                      );
	   removeColumn.addActionListener(
			                              new ActionListener()
			                              {
			                            	  public void actionPerformed(ActionEvent e)
			                            	  {
			                            		  if(table.getSelectedColumn()>=0)
			                            		  {
			                            			  DefaultTableColumnModel model = (DefaultTableColumnModel)table.getColumnModel();
			                            			  TableColumn column = m	odel.getColumn(table.getSelectedColumn());
			                            			  model.removeColumn(column);
			                            		  }
			                            	  }
			                              }
			                         );
	   table.getModel().addTableModelListener(
			                           new TableModelListener()
			                           {
			                        	   public void 	tableChanged(TableModelEvent e)
			                        	   {
			                        		   text.append("Table changed\n\n");
			                        	   }
			                           }
			                      );
	   
	   table.getColumnModel().addColumnModelListener(
			                                             new TableColumnModelListener()
			                                             {
			                                            	 public void columnAdded(TableColumnModelEvent e)
			                                            	 {
			                                            		
			                                            	 }
			                                            	 public void columnRemoved(TableColumnModelEvent e)
			                                            	 {
			                                            		 text.append("Remove column\n\n");
			                                            	 }
			                                            	 public void columnMoved(TableColumnModelEvent e)
			                                            	 {
			                                            		 
			                                            	 }
			                                            	 public void columnSelectionChanged(ListSelectionEvent e)
			                                            	 {
			                                            		
			                                            	 }
			                                            	 public void columnMarginChanged(ChangeEvent e) 
			                                            	 {
			                                            		 
			                                            	 }
			                                             }
			                                        );
	   table.getSelectionModel().addListSelectionListener(
			                                                new ListSelectionListener()
			                                                {
			                                                	 public void valueChanged(ListSelectionEvent e)
				                                                 {
			                                                		 if(e.getValueIsAdjusting())
				                                                	     text.append("("+table.getSelectedRow()+","+table.getSelectedColumn()+")"+"be selected\n\n"); 
				                                                 }
			                                                }
			                                             );
	}
	public Object[][] getTable(String databaseName,String tableName) throws SQLException,ClassNotFoundException
	{
		String sql = "SELECT * FROM "+tableName;
		Connection connection = JdbcUtil.getConnection();
		Statement statement = connection.createStatement();
		ResultSet set = statement.executeQuery(sql);
		ResultSetMetaData metaData = set.getMetaData();
		int rows = getRows(set);
		int columns = metaData.getColumnCount();
		Object[][] table = new Object[rows][columns];
		int i=0;
		while(set.next())
		{
			for(int j=0;j<columns;j++)
			{
				table[i][j]=set.getObject(j+1);
			}
			i++;
		}
		JdbcUtil.free(connection,statement,set);
		return table;
	}
	public int getRows(ResultSet set) throws SQLException
	{
		int count = 0;
		while(set.next())
			count++;
		set.beforeFirst();
		return count;
	}
	public Object[] getColumnNames(String databaseName,String tableName) throws SQLException,ClassNotFoundException
	{
		String sql = "SELECT * FROM "+tableName;
		Connection connection = JdbcUtil.getConnection();
		Statement statement = connection.createStatement();
		ResultSet set = statement.executeQuery(sql);
		ResultSetMetaData metaData = set.getMetaData();
		Object[] columnNames = new Object[metaData.getColumnCount()];
		for(int i=0;i<columnNames.length;i++)
			columnNames[i] = metaData.getColumnName(i+1);
	    JdbcUtil.free(connection,statement,set);
		return columnNames;
	}
}
