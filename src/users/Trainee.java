package users;

import java.time.LocalDate;

public class Trainee extends User {

	private int trainerId;
	private String info;
	
	public Trainee(int id, String username, String gym, LocalDate bod, String gender, String firstName, String lastName,
			String password, String info, int trainerId) {
		super(id, username, gym, bod, gender, firstName, lastName, password);
		this.setTrainerId(trainerId);
		this.setInfo(info);
	}

	public int getTrainerId() {
		return trainerId;
	}

	public void setTrainerId(int trainerId) {
		this.trainerId = trainerId;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

}
