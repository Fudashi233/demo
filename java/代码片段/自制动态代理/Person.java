package cn.edu.jxau.test;

public class Person implements Say{
	
	public Person() {
		
	}
	
	@Override
	public void say() {
		System.out.println("Hello world!");
	}
}
