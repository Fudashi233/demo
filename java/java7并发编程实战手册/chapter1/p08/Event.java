package chapter1.p08;

import java.util.Date;

public class Event {
	
	private String event;
	private Date date;
	public Event(String event) {
		
		this.event = event;
		this.date = new Date();
	}
	
	public Date getDate() {
		
		return date;
	}
	
	public String getEvent() {
		
		return event;
	}
	
	@Override
	public String toString() {
		
		return "date:"+date+"-event:"+event;
	}
}
