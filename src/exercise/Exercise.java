package exercise;

public class Exercise {
	
	private final int id;
	private String name;
	private MuscleGroup muscleGroup;
	private String explantaion;
	private String graphicURL;
	public static final String noImageURL = "Images_Exercises\\no-image.png";
	
	public Exercise(int id, String name, MuscleGroup muscleGroup, String explantaion, String graphicURL) {
		super();
		this.id = id;
		this.name = name;
		this.muscleGroup = muscleGroup;
		this.explantaion = explantaion;
		if(graphicURL == null){
			this.graphicURL = noImageURL;
		}
		else{
			this.graphicURL = graphicURL;			
		}
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public MuscleGroup getMuscleGroup() {
		return muscleGroup;
	}
	public void setMuscleGroup(MuscleGroup muscleGroup) {
		this.muscleGroup = muscleGroup;
	}
	public String getExplantaion() {
		return explantaion;
	}
	public void setExplantaion(String explantaion) {
		this.explantaion = explantaion;
	}
	public String getGraphicURL() {
		return graphicURL;
	}
	public void setGraphicURL(String graphicURL) {
		this.graphicURL = graphicURL;
	}
	public int getId() {
		return id;
	}

}
