package cn.edu.jxau.bean;

public class User {
	
	private String username;
	private String password;
	private int roleID;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getRoleID() {
		return roleID;
	}
	public void setRoleID(int roleID) {
		this.roleID = roleID;
	}
	@Override
	public String toString() {
		return "User [username=" + username + ", password=" + password + ", roleID=" + roleID + "]";
	}
}
