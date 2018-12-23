package exercise;

public enum MuscleGroup {

	Legs("Legs"), Chest("Chest"), Upper_Back("Upper Back"), Lower_Back("Lower Back"),
	Abdominals("Abdominals"), Glutes("Glutes"), Hands("Hands"); 
	
	private String muscleGroupString;
	
	MuscleGroup(String str){
		this.muscleGroupString = str;
	}

	public String toString() {
		return muscleGroupString;
	}
	
	static public MuscleGroup getMuscleGroupEnum(String str){
		for(MuscleGroup mg : MuscleGroup.values()){
			if(mg.toString().equals(str)) return mg;
		}
		return null;
	}
	
}
