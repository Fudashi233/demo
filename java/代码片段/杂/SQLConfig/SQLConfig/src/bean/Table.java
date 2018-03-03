package bean;

public class Table {
	
	private String tableName;
	private Column[] columns;
	private String key;		//主键
	public String getTableName() {
		
		return tableName;
	}
	public void setTableName(String tableName) {
		
		this.tableName = tableName;
	}
	public Column[] getColumns() {
		
		return columns;
	}
	public void setColumns(Column[] columns) {
		
		this.columns = columns;
	}
	public String getKey() {
		
		return key;
	}
	public void setKey(String key) {
		
		this.key = key;
	}
	
}
