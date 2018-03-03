package cn.edu.jxau.lang;

import java.util.EventListener;

public interface ClickListener extends EventListener {
	
	void action(ClickEvent event);
}
