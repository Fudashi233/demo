package test;

public class Employee {

	private int id;
	private String name;
	private String age;
	public Employee() {
		try {
			ThreadUtil.randomSleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAge() {
		return age;
	}
	
	public void setAge(String age) {
		this.age = age;
	}
}	
