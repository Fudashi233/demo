package cn.edu.jxau.bean;

public class Result {
	private String type;
	private String destinationURI;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDestinationURI() {
		return destinationURI;
	}
	public void setDestinationURI(String destinationURI) {
		this.destinationURI = destinationURI;
	}
	@Override
	public String toString() {
		return "Result [type=" + type + ", URI=" + destinationURI + "]";
	}
}
