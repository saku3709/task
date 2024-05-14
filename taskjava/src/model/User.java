package model;

public class User {

	public static final class Entity {
		public static final String TBL_USER = "task_user";
		public static final String COL_ID = "user_id";
		public static final String COL_NAME = "user_name";
		public static final String COL_PASSWORD = "user_password";
	}

	private String id;
	private String name;
	private String password;

	public User() {
	}

	public User(String id,String name, String password) {
		this.id = id;
		this.name = name;
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "User [name=" + name + ", id=" + id + ", password=" + password + "]";
	}

}
