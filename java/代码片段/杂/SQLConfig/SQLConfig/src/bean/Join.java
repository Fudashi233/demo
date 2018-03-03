package bean;

public class Join {

	private Table leftTable;
	private Table rightTable;
	private String type;
	public Table getLeftTable() {
		
		return leftTable;
	}
	public void setLeftTable(Table leftTable) {
		
		this.leftTable = leftTable;
	}
	public Table getRightTable() {
		
		return rightTable;
	}
	public void setRightTable(Table rightTable) {
		
		this.rightTable = rightTable;
	}
	public String getType() {
		
		return type;
	}
	public void setType(String type) {
		
		this.type = type;
	}
	
	@Override
	public String toString() {
		
		return leftTable.getTableName()+"	"+leftTable.getKey()+"	"+
				rightTable.getTableName()+"	"+rightTable.getKey()+"	"+
				type;
	}
}
