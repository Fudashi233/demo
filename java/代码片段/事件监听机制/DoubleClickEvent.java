package cn.edu.jxau.lang;

import java.util.EventObject;

public class DoubleClickEvent extends EventObject {

	private String info;

	public DoubleClickEvent(Object source, String info) {
		super(source);
		this.info = info;
	}

	public String getInfo() {
		return info;
	}
}
