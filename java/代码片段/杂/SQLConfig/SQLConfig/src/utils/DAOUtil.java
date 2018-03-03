package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import bean.Column;
import bean.Condition;
import bean.Join;
import bean.Operation;
import bean.Order;
import bean.Table;
import bean.Type;

public class DAOUtil {

	private static final String CONFIG_PATH = "SQLConfig.xml";
	public static Statement getSQL(String name,Map<String,Object> paramMap) throws DocumentException, SQLException {
		
		if(name==null || paramMap==null)
			return null;
		Document document = XMLUtil.parse(CONFIG_PATH);
		Element element = XMLUtil.getElementByName(document,name);
		Operation operation = element2Operation(element);
		String SQL = null;
		switch(operation.getType()) {
		
			case INSERT :SQL = getInsertSQL(operation);break;
			case DELETE :SQL = getDeleteSQL(operation);break;
			case UPDATE :SQL = getUpdateSQL(operation);break;
			case SELECT :SQL = getSelectSQL(operation);break;
		}
		if(SQL!=null) {
			
			System.out.println("40---"+SQL);
			Connection connection = JDBCUtil.getConnection();
			Statement statement = connection.prepareStatement(SQL);
			if(operation.getType().equals(Type.INSERT)) {//特殊处理INSERT语句
				
				return getInsertPreparedParam((PreparedStatement)statement,operation,paramMap);
			} else if(operation.getType().equals(Type.UPDATE)) {//特殊处理UPDATE语句
				
				return getUpdatePreparedParam((PreparedStatement)statement,operation,paramMap);
			} else {
				
				return getPreparedParam((PreparedStatement)statement,operation,paramMap);
			}
			
		}
		return null;
	}
	
	private static Statement getPreparedParam(PreparedStatement statement,Operation operation,Map<String,Object> paramMap) throws SQLException {
		
		if(paramMap==null)
			return null;
		operation.getTables()[0].getColumns();
		Condition[] conditionArr = operation.getConditions();
		for(int i=0;i<conditionArr.length;i++) {
			
			Condition condition = conditionArr[i];
			String key = condition.getMapName();
			//如果mapName为空，则使用columnName当做key
			if(key==null) {
				
				key = condition.getColumnName();
			}
			statement.setObject(i+1,paramMap.get(key));
		}
		return statement;
	}
	
	private static Statement getUpdatePreparedParam(PreparedStatement statement,Operation operation,Map<String,Object> paramMap) throws SQLException {
		
		if(paramMap==null)
			return null;
		Column[] columnArr = operation.getTables()[0].getColumns();
		int count = 1;
		if(columnArr!=null && columnArr.length!=0) {
			
			for(int i=0;i<columnArr.length;i++) {
				
				Column column = columnArr[i];
				String key = column.getMapName();
				if(key==null) {//如果mapName为空，则使用column的name当做key
					
					key = column.getName();
				}
				statement.setObject(count,paramMap.get(key));
				count++;
			}
		}
		Condition[] conditionArr = operation.getConditions();
		for(int i=0;i<conditionArr.length;i++) {
			
			Condition condition = conditionArr[i];
			String key = condition.getMapName();
			if(key==null) {//如果mapName为空，则使用columnName当做key
				
				key = condition.getColumnName();
			}
			statement.setObject(count,paramMap.get(key));
			count++;
		}
		return statement;
	}
	
	private static Statement getInsertPreparedParam(PreparedStatement statement,Operation operation,Map<String,Object> paramMap) throws SQLException {
		
		if(paramMap==null)
			return null;
		Column[] columnArr = operation.getTables()[0].getColumns();
		if(columnArr!=null && columnArr.length!=0) {
			
			for(int i=0;i<columnArr.length;i++) {
				
				Column column = columnArr[i];
				String key = column.getMapName();
				if(key==null) {//如果mapName为空，则使用column的name当做key
					
					key = column.getName();
				}
				statement.setObject(i+1,paramMap.get(key));
			}
			return statement;
		}
		return null;
	}
	
	/**
	 * 构造例如“INSERT INTO view1 VALUES(2,"awd","WANS")”的SQL语句
	 * 
	 * @param operation
	 * @return SQL语句
	 */
	private static String getInsertSQL(Operation operation) {
		
		//构造第一部分的SQL语句
		StringBuilder insertSQL = new StringBuilder("INSERT INTO ");
		
		//构造表名
		String tableName = operation.getTables()[0].getTableName();
		
		//构造插入参数
		Column[] columnArr = operation.getTables()[0].getColumns();
		if(columnArr!=null && columnArr.length!=0) {
			
			StringBuilder insertParam = new StringBuilder("(");
			for(int i=0;i<columnArr.length;i++) {
				
				Column column = columnArr[i];
				insertParam.append(column.getName()+",");
			}
			insertParam.deleteCharAt(insertParam.length()-1);	//删除最后一个对于的逗号
			insertParam.append(") VALUES(?");
			for(int i=0;i<columnArr.length-1;i++) {
				
				insertParam.append(",?");
			}
			insertParam.append(")");
			
			//拼接SQL语句
			insertSQL.append(tableName+" ");
			insertSQL.append(insertParam);
			return insertSQL.toString();
		}
		return null;
	}
	/**
	 * 构造例如“DELETE FROM student WHERE id>0 AND id<8”的SQL语句
	 * 
	 * @param operation
	 * @return SQL语句
	 */
	private static String getDeleteSQL(Operation operation) {
		
		//构造第一部分的SQL语句
		StringBuilder deleteSQL = new StringBuilder("DELETE FROM ");
		
		//构造表名
		String tableName = operation.getTables()[0].getTableName();
		
		//构造删除条件
		StringBuilder conditions = null;
		if(operation.getConditions()!=null) {
			
			conditions = new StringBuilder("WHERE 1=1 ");
			Condition[] conditionArr = operation.getConditions();
			for(int i=0;i<conditionArr.length;i++) {
				
				Condition condition = conditionArr[i];
				conditions.append(condition.getConnection());
				conditions.append(" ");
				if(condition.getTableName()!=null) {
					
					conditions.append(condition.getTableName()+".");
				}
				conditions.append(condition.getColumnName()+condition.getSign()+"? ");
			}
		}
		deleteSQL.append(tableName+" ");
		if(conditions!=null) {
			
			deleteSQL.append(conditions);
		}
		return deleteSQL.toString();
	}
	
	private static String getUpdateSQL(Operation operation) {
		
		//构造第一部分SQL
		StringBuilder updateSQL = new StringBuilder(" UPDATE ");
		
		//构造表名
		String tableName = operation.getTables()[0].getTableName();
		updateSQL.append(tableName);
		updateSQL.append(" SET ");
		
		//构造更新的目的属性
		Column[] columns = operation.getTables()[0].getColumns();
		for(int i=0;i<columns.length;i++) {
			
			Column column = columns[i];
			String columnName = column.getName();
			if(columnName!=null && !"".equals(columnName.trim())) {
				
				updateSQL.append(columnName);
				updateSQL.append("=?,");
			}
		}
		updateSQL.deleteCharAt(updateSQL.length()-1);//删除最后一个逗号
		
		//构造条件
		Condition[] conditions = operation.getConditions();
		if(conditions!=null) {
			
			updateSQL.append(" WHERE 1=1 ");
			for(int i=0;i<conditions.length;i++) {
				
				Condition condition = conditions[i];
				updateSQL.append(condition.getConnection());
				updateSQL.append(" ");
				String conditionTableName = condition.getTableName();
				if(conditionTableName!=null && !"".equals(conditionTableName.trim())) {
					
					updateSQL.append(conditionTableName);
					updateSQL.append(".");
				}
				updateSQL.append(condition.getColumnName());
				updateSQL.append(condition.getSign());
				updateSQL.append("?");
			}
		}

		return updateSQL.toString();
	}
	
	
	/**
	 * 构造出例如“SELECT t_user.userID,t_favorite.favoriteID,t_spectrum.spectrumID FROM
	 * t_user INNER JOIN t_favorite ON t_user.userID = t_favorite.userID
	 * INNER JOIN t_favoriteSpectrum ON t_favorite.favoriteID = t_favoriteSpectrum.favoriteID
     * INNER JOIN t_spectrum ON t_favoriteSpectrum.spectrumID = t_spectrum.spectrumID
     * WHERE t_user.userID = 2 
     * ORDER BY age DESC”查询SQL语句
     * 
	 * @param operation
	 * @return
	 */
	private static String getSelectSQL(Operation operation) {
	
		
		//构造要查询出的部分
		StringBuilder selectBuilder = new StringBuilder("SELECT ");
		Table[] tableArr = operation.getTables();
		for(int i=0;i<tableArr.length;i++) {
			
			Table table = tableArr[i];
			Column[] columnArr = table.getColumns();
			for(int j=0;j<columnArr.length;j++) {
				
				Column column = columnArr[j];
				if(table.getTableName()!=null) {
					
					selectBuilder.append(table.getTableName()+"."+column.getName()+",");
				} else {
					
					selectBuilder.append(column.getName()+",");
				}
			}
		}
		selectBuilder.deleteCharAt(selectBuilder.length()-1);
		selectBuilder.append(" ");
		System.out.println("220---"+selectBuilder);
		
		//构造连接的表  ... JOIN ...
		StringBuilder joinBuilder = null;
		if(operation.getJoins()!=null) {
			
			joinBuilder = new StringBuilder();
			Join[] joinArr = operation.getJoins();
			joinBuilder.append(joinArr[0].getLeftTable()+" ");
			for(int i=0;i<joinArr.length;i++) {
				
				Join join = joinArr[i];
				joinBuilder.append(" "+join.getType()+" ");
				joinBuilder.append(join.getRightTable().getTableName());
				joinBuilder.append(" ON ");
				joinBuilder.append(join.getLeftTable().getTableName()+"."+join.getLeftTable().getKey());
				joinBuilder.append("=");
				joinBuilder.append(join.getRightTable().getTableName()+"."+join.getRightTable().getKey()+" ");
			}
			System.out.println("236---"+joinBuilder);
		} else {	//	如果为空，则表示单表查询，继续拼接selectBuilder
			
			selectBuilder.append("FROM ");
			selectBuilder.append(tableArr[0].getTableName());
			selectBuilder.append(" ");
		}
		
		
		//构造条件:WHERE 1=1 AND ...
		StringBuilder conditionBuilder = null;
		if(operation.getConditions()!=null) {
			
			conditionBuilder = new StringBuilder("WHERE 1=1 ");
			Condition[] conditionArr = operation.getConditions();
			for(int i=0;i<conditionArr.length;i++) {
				
				Condition condition = conditionArr[i];
				conditionBuilder.append(condition.getConnection()+" ");
				if(condition.getTableName()!=null) {
					
					conditionBuilder.append(condition.getTableName()+".");
				}
				conditionBuilder.append(condition.getColumnName());
				conditionBuilder.append(condition.getSign()+"? ");		
			}
			System.out.println("250---"+conditionBuilder);
		}
		
		
		//构造显示序列:ORDER BY age DESC
		StringBuilder orderByBuilder = null ;
		if(operation.getOrders()!=null) {
			
			orderByBuilder = new StringBuilder("ORDER BY ");
			Order[] orderArr = operation.getOrders();
			for(int i=0;i<orderArr.length;i++) {
				
				Order order =orderArr[i];
				if(order.getTableName()!=null) {
					
					orderByBuilder.append(order.getTableName()+".");
				}
				orderByBuilder.append(order.getColumnName()+" ");
				if(order.isDESC()) {	//如果是降序
					
					orderByBuilder.append("DESC ");
				} else {	//否则就是升序
					
					orderByBuilder.append("ASC ");
				}
			}		
			System.out.println("270---"+orderByBuilder);
		} 
		
		
		//拼接各部分
		StringBuilder SQL = new StringBuilder();
		SQL.append(selectBuilder);
		if(joinBuilder!=null) {
			
			SQL.append(joinBuilder);
		}
		if(conditionBuilder!=null) {
			
			SQL.append(conditionBuilder);
		}
		if(orderByBuilder!=null) {
			
			SQL.append(orderByBuilder);
		}
		
		
		return SQL.toString();
	}
	
	private static Operation element2Operation(Element element) {
		
		Operation operation = new Operation();
		//设置operation的值
		operation.setName(element.attribute("name").getValue());
		operation.setType(element.attribute("type").getValue());
		if(element.element("tables")!=null) {
			
			operation.setTables(element2Tables(element.element("tables")));
		}
		if(element.element("conditions")!=null) {
			
			operation.setConditions(element2Conditions(element.element("conditions")));
		}
		if(element.element("joins")!=null) {
			
			operation.setJoins(element2Joins(element.element("joins")));
		}
		if(element.element("orders")!=null) {
			
			operation.setOrders(element2Orders(element.element("orders")));
		}	
		return operation;
	}
	
	public static Table[] element2Tables(Element element) {
		
		Iterator<Element> iterator =element.elementIterator("table");
		ArrayList<Table> tableList = new ArrayList<Table>();
		while(iterator.hasNext()) {
			
			Element tableElement = iterator.next();
			Table table = new Table();
			//设置tableName
			table.setTableName(tableElement.attributeValue("tableName"));
			
			//设置columns
			ArrayList<Column> columnList = new ArrayList<Column>();
			Iterator<Element> columnIterator = tableElement.elementIterator("column");
			while(columnIterator.hasNext()) {
				
				Element columnElement = columnIterator.next();
				Column column = new Column();
				if(columnElement.element("name")!=null) {
					
					column.setName(columnElement.element("name").getText());
				}
				if(columnElement.element("mapName")!=null) {
					
					column.setMapName(columnElement.element("mapName").getText());
				}	
				columnList.add(column);
			}
			table.setColumns(columnList.toArray(new Column[0]));
			
			//设置key
			if(tableElement.element("key")!=null) {
				
				table.setKey(tableElement.element("key").getText());
			}
			tableList.add(table);
			
		}
		return tableList.toArray(new Table[0]);
	}
	
	public static Condition[] element2Conditions(Element element) {
		
		Iterator<Element> iterator = element.elementIterator("condition");
		ArrayList<Condition> conditionList = new ArrayList<Condition>();
		while(iterator.hasNext()) {
			
			Element conditionElement = iterator.next();
			Condition condition = new Condition();
			if(conditionElement.element("tableName")!=null) {
				
				condition.setTableName(conditionElement.element("tableName").getText());
			}
			if(conditionElement.element("columnName")!=null) {
				
				condition.setColumnName(conditionElement.element("columnName").getText());
			}
			if(conditionElement.element("mapName")!=null) {
				
				condition.setMapName(conditionElement.element("mapName").getText());
			}
			if(conditionElement.element("connection")!=null) {
				
				condition.setConnection(conditionElement.element("connection").getText());
			}
			if(conditionElement.element("sign")!=null){
				
				condition.setSign(conditionElement.element("sign").getText());
			} else {
				
				condition.setSign("=");
			}
			conditionList.add(condition);
		}
		return conditionList.toArray(new Condition[0]);
	}
	
	public static Join[] element2Joins(Element element) {
		
		Iterator<Element> iterator = element.elementIterator("join");
		ArrayList<Join> joinList = new ArrayList<Join>();
		while(iterator.hasNext()) {
			
			Element joinElement = iterator.next();
			Join join = new Join();
			
			//设置joinType
			if(joinElement.element("type")!=null) {
				
				join.setType(joinElement.element("type").getText());
			}
			//设置table
			Iterator<Element> tableIterator = joinElement.elementIterator("table");
			//设置left table
			if(tableIterator.hasNext()) {
				
				System.out.println("302---left table");
				Element table = tableIterator.next();
				Table leftTable = new Table();
				leftTable.setTableName(table.attributeValue("tableName"));
				if(table.element("key")!=null) {
					
					leftTable.setKey(table.element("key").getText());
				}
				join.setLeftTable(leftTable);
			}
			//设置right table
			if(tableIterator.hasNext()) {
				
				System.out.println("314---right table");
				Element table = tableIterator.next();
				Table rightTable = new Table();
				rightTable.setTableName(table.attributeValue("tableName"));
				if(table.element("key")!=null) {
					
					rightTable.setKey(table.element("key").getText());
				}
				join.setRightTable(rightTable);
			}
			joinList.add(join);
			
		}
		return joinList.toArray(new Join[0]);
	}
	
	public static Order[] element2Orders(Element element) {
		
		Iterator<Element> iterator = element.elementIterator("order");
		ArrayList<Order> orderList = new ArrayList<Order>();
		while(iterator.hasNext()) {
			
			Element orderElement = iterator.next();
			Order order = new Order();
			
			//设置table name
			if(orderElement.element("tableName")!=null) {
				
				order.setTableName(orderElement.element("tableName").getText());
			}
			//设置column name
			if(orderElement.element("columnName")!=null) {
				
				order.setColumnName(orderElement.element("columnName").getText());
			}
			//设置升序还是降序
			if(orderElement.element("DESC")!=null) {
				
				order.setDESC(Boolean.valueOf(orderElement.element("columnName").getText()));
			}
			orderList.add(order);
		}
		return orderList.toArray(new Order[0]);
	}
}
