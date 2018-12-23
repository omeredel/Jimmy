package users;

import java.time.LocalDate;

abstract class User {
	private int id;
	private String username;
	private String firstName;
	private String lastName;
	private String gym;
	private String password;
	private LocalDate bod;
	private String gender;
	
	public User(int id, String username, String gym, LocalDate bod, String gender, String firstName, String lastName, String password) {
		super();
		this.id = id;
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gym = gym;
		this.bod = bod;
		this.gender = gender;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getGym() {
		return gym;
	}

	public void setGym(String gym) {
		this.gym = gym;
	}

	public LocalDate getBod() {
		return bod;
	}

	public void setBod(LocalDate bod) {
		this.bod = bod;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
