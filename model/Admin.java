package model;


public class Admin {
	private Integer adminID;
	private String  username;
	private String  password;

	public Admin() {
		super();
	}

	public Admin(Integer adminID, String username, String password) {
		super();
		this.adminID = adminID;
		this.username = username;
		this.password = password;
	}

	public Integer getAdminID() {
		return this.adminID;
	}

	public void setAdminID(Integer adminID) {
		this.adminID = adminID;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
