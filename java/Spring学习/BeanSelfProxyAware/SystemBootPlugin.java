package cn.edu.jxau.lang;

import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

public interface SystemBootPlugin extends Ordered {
	
	void onReady();
}
