package controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import DBConnection.DBHandler;
import exercise.Exercise;
import exercise.MuscleGroup;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import users.Trainee;
import users.Trainer;

public class TrainerMainControllers {

	@FXML
	private JFXTextField firstnameTxt_bio;

	@FXML
	private JFXTextField lastnameTxt_bio;

	@FXML
	private JFXTextField gymTxt_bio;

	@FXML
	private JFXDatePicker bodDate_bio;

	@FXML
	private JFXRadioButton maleRdio_bio;

	@FXML
	private ToggleGroup gender;

	@FXML
	private JFXRadioButton femaleRdio_bio;

	@FXML
	private JFXRadioButton didRdio_bio;

	@FXML
	private JFXButton saveBtn_bio;

	@FXML
	private JFXButton resetBtn_bio;

    @FXML
    private Label statusLbl_bio;
	
	@FXML
	private JFXComboBox<String> traineeLst;

	@FXML
	private Label firstnameLbl_tr;

	@FXML
	private Label lastnameLbl_tr;

	@FXML
	private Label gymLbl_tr;

	@FXML
	private Label dobLbl_tr;

	@FXML
	private Label genderLbl_tr;

	@FXML
	private Label firstnameAns_tr;

	@FXML
	private Label lastnameAns_tr;

	@FXML
	private Label gymAns_tr;

	@FXML
	private Label dobAns_tr;

	@FXML
	private Label genderAns_tr;

	@FXML
	private Label infoLbl_tr;

	@FXML
	private TextArea infoBox_tr;

    @FXML
    private JFXComboBox<String> muscleGroupLst_Exr;

    @FXML
    private JFXButton addBtn_Exr;

    @FXML
    private JFXListView<Exercise> exercisesLst_Exr;
    
    private ObservableList<Exercise> obsLst;

	private Connection connection;
	private DBHandler dbhandler;
	private PreparedStatement pst;

	private Trainer currentTrainer;

	private Trainee selectedTrainee;

	static class Cell extends ListCell<Exercise>
	{
		HBox hbox = new HBox();
		Label nameLbl = new Label("");
		Label explLbl = new Label("");
		ImageView graphicExpl = new ImageView("Images_Exercises\\row.jpg");
		JFXButton editBtn = new JFXButton("Edit Exercise");
		Pane pane1 = new Pane();
		Pane pane2 = new Pane();
		Pane pane3 = new Pane();
		
		public Cell(){
			super();
			hbox.getChildren().addAll(nameLbl, pane1, explLbl,pane2, graphicExpl,pane3, editBtn);
			HBox.setHgrow(pane1, Priority.ALWAYS);
			HBox.setHgrow(pane2, Priority.ALWAYS);
			HBox.setHgrow(pane3, Priority.ALWAYS);
			nameLbl.setFont(Font.font("System", 25.0));
			nameLbl.setTextFill(Paint.valueOf("#5c5757"));
			nameLbl.setAlignment(Pos.CENTER);
			nameLbl.setMinSize(200, 150);
			nameLbl.setLayoutY(20);
			explLbl.setFont(Font.font("System", 20.0));
			explLbl.setTextFill(Paint.valueOf("#5c5757"));
			explLbl.setAlignment(Pos.CENTER);
			explLbl.setMinSize(200, 150);
			explLbl.setLayoutY(20);
			editBtn.setAlignment(Pos.CENTER);
			editBtn.setStyle("-fx-background-color: #62929a;");
			editBtn.setTextFill(Paint.valueOf("#efecec"));
			editBtn.setOnAction(e -> System.out.println(getItem().getId()));
			graphicExpl.setOnMouseClicked(e -> System.out.println(getItem().getGraphicURL()));
		}
		
		public void updateItem(Exercise exercise, boolean empty){
			super.updateItem(exercise, empty);
			setText(null);
			setGraphic(null);
			if(exercise!=null && !empty){
				nameLbl.setText(exercise.getName());
				explLbl.setText(exercise.getExplantaion());
				graphicExpl.setImage(new Image(exercise.getGraphicURL()));
				graphicExpl.setFitHeight(150);		
				graphicExpl.setFitWidth(150);		
				setGraphic(hbox);
			}
		}
	}
	
	@FXML
	protected void initialize() {
		dbhandler = new DBHandler();
		connection = dbhandler.getConnection();
		initCurrentTrainer();
		updateBioUI();
		initTraineesTab();
		initExercisesTab();
	}
	
	private void initExercisesTab() {
		muscleGroupLst_Exr.getItems().add("Choose a muscle group...");
		for(MuscleGroup mg : MuscleGroup.values()){
			muscleGroupLst_Exr.getItems().add(mg.toString());
		}
		if(!muscleGroupLst_Exr.getItems().isEmpty()){
			muscleGroupLst_Exr.setValue(muscleGroupLst_Exr.getItems().get(0));
		}	
	}

	private void initExercisesViewList(MuscleGroup mg) {
		List<Exercise> lst = getExercisesList(mg);
		obsLst = FXCollections.observableArrayList(lst);
		exercisesLst_Exr.setItems(obsLst);
		GridPane pane = new GridPane();
		Label name = new Label("");
		JFXButton btn = new JFXButton("");
		pane.add(name, 0, 0);
		pane.add(btn, 0, 1);
		exercisesLst_Exr.setCellFactory(param -> new Cell());
	}

	private ArrayList<Exercise> getExercisesList(MuscleGroup mg) {
		ArrayList<Exercise> exercisesList = new ArrayList<Exercise>();
		String selectExercisesByMuscleGroup = "SELECT * FROM exercises WHERE muscleGroup = '" + mg.toString() + "'";
		try {
			pst = connection.prepareStatement(selectExercisesByMuscleGroup);
			ResultSet rs = pst.executeQuery(selectExercisesByMuscleGroup);
			while(rs.next()){
				exercisesList.add(new Exercise(rs.getInt(1), rs.getString(2), MuscleGroup.getMuscleGroupEnum(rs.getString(3)),
						rs.getString(4), rs.getString(5)));
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return exercisesList;			
	}

	@FXML
    void onSelectionMuscleGroupChoiceBox(ActionEvent event) {
		MuscleGroup mg = MuscleGroup.getMuscleGroupEnum(muscleGroupLst_Exr.getSelectionModel().getSelectedItem());
		if(mg != null){
			initExercisesViewList(mg);
		}
    }	
	
	@FXML
	public void saveChangesAction(ActionEvent e) throws IOException, SQLException{
		String changeTrainerBio = "UPDATE trainers SET TrainerGym = ?, "
				+ "TrainerBOD=?, TrainerGender=?, TrainerFirstName=?, TrainerLastName=?";
		
		statusLbl_bio.setVisible(false);
		if(!allFieldsValid()) return;
		try {
			pst = connection.prepareStatement(changeTrainerBio);
			pst.setString(1, gymTxt_bio.getText());
			pst.setDate(2, Date.valueOf(bodDate_bio.getValue()));
			pst.setString(3, getSelectedRadio());
			pst.setString(4, firstnameTxt_bio.getText());
			pst.setString(5, lastnameTxt_bio.getText());
			pst.executeUpdate();
			statusLbl_bio.setVisible(true);
			statusLbl_bio.setText("Updated!");
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	private boolean allFieldsValid() {
		boolean isEmpty = false;
		if(firstnameTxt_bio.getText().isEmpty()){
			isEmpty = true;
		}
		if(lastnameTxt_bio.getText().isEmpty()){
			isEmpty = true;
		}
		if(gymTxt_bio.getText().isEmpty()){
			isEmpty = true;
		}
		if(bodDate_bio.getValue() == null){
			isEmpty = true;
		}
		if(isEmpty){
			statusLbl_bio.setVisible(true);
			statusLbl_bio.setText("Some of the fields are empty!");
			return false;
		}
		else return true;
	}

	private String getSelectedRadio() {
		if (maleRdio_bio.isSelected()) return "Male";
		else if (femaleRdio_bio.isSelected()) return "Female";
		else return "None";
	}
	
	@FXML
	public void resetBtnAction(ActionEvent e){
		updateBioUI();
	}
	
	private void initCurrentTrainer() {
		String tempUsername = LoginControllers.getInstance().getLoggedInTrainerUsername();
		String selectDataByUsername = "SELECT * FROM trainers WHERE TrainerUsername = '" + tempUsername + "'";
		try {
			pst = connection.prepareStatement(selectDataByUsername);
			ResultSet rs = pst.executeQuery(selectDataByUsername);
			while(rs.next()){
				currentTrainer = new Trainer(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getDate(4).toLocalDate(),
						rs.getString(5),rs.getString(6),rs.getString(7), rs.getString(8));
			}
			String getTraineesByTrainerId = "SELECT * FROM trainees WHERE idTrainer = " + this.currentTrainer.getId();
			pst = connection.prepareStatement(getTraineesByTrainerId);
			rs = pst.executeQuery(getTraineesByTrainerId);
			while(rs.next()){
				this.currentTrainer.addTrainee(new Trainee(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getDate(4).toLocalDate(),
						rs.getString(5),rs.getString(6),rs.getString(7), rs.getString(8), rs.getString(9), rs.getInt(10)));
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}	
	}

	private void initTraineesTab() {
		hideAllLabels();
		traineeLst.getItems().add("Choose a trainee...");
		for(Trainee tr : this.currentTrainer.getTrainees()){
			String fullTraineeName = tr.getFirstName() + " " + tr.getLastName();
			traineeLst.getItems().add(fullTraineeName);
		}
		if(!traineeLst.getItems().isEmpty()){
			traineeLst.setValue(traineeLst.getItems().get(0));
		}
	}

	private void hideAllLabels() {
		firstnameAns_tr.setVisible(false);
		lastnameAns_tr.setVisible(false);
		gymAns_tr.setVisible(false);
		dobAns_tr.setVisible(false);
		genderAns_tr.setVisible(false);
		infoBox_tr.setVisible(false);
		firstnameLbl_tr.setVisible(false);
		lastnameLbl_tr.setVisible(false);
		gymLbl_tr.setVisible(false);
		dobLbl_tr.setVisible(false);
		genderLbl_tr.setVisible(false);
		infoLbl_tr.setVisible(false);
	}

	public void onSelectionTraineeChoiceBox(){
		int traineeListIndex = traineeLst.getSelectionModel().getSelectedIndex() - 1;
		if(traineeListIndex > -1){
			this.selectedTrainee = this.currentTrainer.getTrainees().get(traineeListIndex);
			updateTraineeUI();
		}
	}
	
	private void updateTraineeUI() {
		firstnameAns_tr.setText(this.selectedTrainee.getFirstName());		
		firstnameAns_tr.setVisible(true);
		lastnameAns_tr.setText(this.selectedTrainee.getLastName());
		lastnameAns_tr.setVisible(true);
		gymAns_tr.setText(this.selectedTrainee.getGym());
		gymAns_tr.setVisible(true);
		dobAns_tr.setText(this.selectedTrainee.getBod().format(DateTimeFormatter.ISO_DATE));
		dobAns_tr.setVisible(true);
		genderAns_tr.setText(this.selectedTrainee.getGender());
		genderAns_tr.setVisible(true);
		infoBox_tr.setText(this.selectedTrainee.getInfo());
		infoBox_tr.setVisible(true);
		firstnameLbl_tr.setVisible(true);
		lastnameLbl_tr.setVisible(true);
		gymLbl_tr.setVisible(true);
		dobLbl_tr.setVisible(true);
		genderLbl_tr.setVisible(true);
		infoLbl_tr.setVisible(true);
	}

	private void updateBioUI() {
		statusLbl_bio.setVisible(false);
		gymTxt_bio.setText(this.currentTrainer.getGym());
		bodDate_bio.setValue(this.currentTrainer.getBod());
		firstnameTxt_bio.setText(this.currentTrainer.getFirstName());
		lastnameTxt_bio.setText(this.currentTrainer.getLastName());
		updateGenderUI();
	}

	private void updateGenderUI() {
		if(currentTrainer.getGender().equals("Male")){
			maleRdio_bio.setSelected(true);
		}
		else if (currentTrainer.getGender().equals("Female")) {
			femaleRdio_bio.setSelected(true);
		}
		else{
			didRdio_bio.setSelected(true);
		}
	}
}
