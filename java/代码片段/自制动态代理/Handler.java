package cn.edu.jxau.test;

import java.lang.reflect.Method;

public interface Handler {
	
	public Object invoke(Object instance,Method method) throws Exception ;
}
