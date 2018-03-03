package bean;

public class Operation {
	
	private String name;
	private Type type;
	private Table[] tables;
	private Condition[] conditions;
	private Join[] joins;
	private Order[] orders;
	
	public String getName() {
		
		return name;
	}
	
	public void setName(String name) {
		
		this.name = name;
	}
	
	public Type getType() {
		
		return type;
	}
	
	public void setType(Type type) {
		
		this.type = type;
	}
	
	public void setType(String type) {
		
		if("insert".equalsIgnoreCase(type)) {
			
			setType(Type.INSERT);
		} else if("delete".equalsIgnoreCase(type)) {
			
			setType(Type.DELETE);
		} else if("update".equalsIgnoreCase(type)) {
			
			setType(Type.UPDATE);
		} else if ("select".equalsIgnoreCase(type)) {
			
			setType(Type.SELECT);
		}
	}
	public Table[] getTables() {
		
		return tables;
	}
	
	public void setTables(Table[] tables) {
		
		this.tables = tables;
	}
	
	public Condition[] getConditions() {
		
		return conditions;
	}
	
	public void setConditions(Condition[] conditions) {
		
		this.conditions = conditions;
	}
	
	public Join[] getJoins() {
		
		return joins;
	}
	
	public void setJoins(Join[] joins) {
		
		this.joins = joins;
	}
	
	public Order[] getOrders() {
		
		return orders;
	}
	
	public void setOrders(Order[] orders) {
		
		this.orders = orders;
	}
}
