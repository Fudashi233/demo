package bean;

public class Order {
	
	private String tableName;
	private String columnName;
	private boolean isDESC;
	public String getTableName() {
		
		return tableName;
	}
	public void setTableName(String tableName) {
		
		this.tableName = tableName;
	}
	public String getColumnName() {
		
		return columnName;
	}
	public void setColumnName(String columnName) {
		
		this.columnName = columnName;
	}
	
	public boolean isDESC() {
		
		return isDESC;
	}
	public void setDESC(boolean isDESC) {
		
		this.isDESC = isDESC;
	}
	@Override
	public String toString() {
		
		return tableName+"	"+columnName;
	}
}
