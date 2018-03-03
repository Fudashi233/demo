package bean;

public class Condition {

	private String tableName;
	private String columnName;
	private String mapName;
	private String connection;
	private String sign;
	
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
	public String getMapName() {
		
		return mapName;
	}
	public void setMapName(String mapName) {
		
		this.mapName = mapName;
	}
	public String getConnection() {
		
		return connection;
	}
	public void setConnection(String connection) {
		
		this.connection = connection;
	}
	public String getSign() {
		
		return sign;
	}
	public void setSign(String sign) {
		
		this.sign = sign;
	}
	@Override
	public String toString() {
		
		return tableName+"	"+columnName+"	"+mapName+"	"+connection+"	"+sign;
	}
}
