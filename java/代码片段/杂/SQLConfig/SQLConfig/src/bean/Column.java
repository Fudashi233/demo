package bean;

public class Column {

	private String name;
	private String mapName;
	public String getName() {
		
		return name;
	}
	public void setName(String name) {
		
		this.name = name;
	}
	public String getMapName() {
		
		return mapName;
	}
	public void setMapName(String mapName) {
		
		this.mapName = mapName;
	}
	@Override
	public String toString() {
		
		return name+"	"+mapName;
	}
}
