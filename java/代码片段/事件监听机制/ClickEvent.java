package cn.edu.jxau.lang;

import java.util.EventObject;

public class ClickEvent extends EventObject {
	
	private String info;
	public ClickEvent(Object source,String info) {
		super(source);
		this.info = info;
	}
	
	public String getInfo() {
		return info;
	}
}
