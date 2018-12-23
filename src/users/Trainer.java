package users;

import java.time.LocalDate;
import java.util.ArrayList;


public class Trainer extends User {
	
	private ArrayList<Trainee> trainees;
	
	public Trainer(int id, String username, String gym, LocalDate bod, String gender, String firstName, String lastName, String password) {
		super(id, username, gym, bod, gender, firstName, lastName, password);
		trainees = new ArrayList<>();
	}

	public void updateTrainees(Trainee[] newTrainees){
		for(Trainee newt : newTrainees){ 
			this.getTrainees().add(newt);
		}
	}

	public ArrayList<Trainee> getTrainees() {
		return trainees;
	}

	public void setTrainees(ArrayList<Trainee> trainees) {
		this.trainees = trainees;
	}
	
	public void addTrainee(Trainee trainee) {
		this.trainees.add(trainee);
	}

}
