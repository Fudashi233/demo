package cn.edu.jxau.lang;

import java.io.IOException;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

	public static void main(String[] args) throws IOException, InterruptedException {

		AbstractApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
		Waiter w = (Waiter) context.getBean("waiter");
		w.greetTo("Fudashi");
	}
}