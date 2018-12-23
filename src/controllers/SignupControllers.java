package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import DBConnection.DBHandler;

public class SignupControllers implements Initializable{

    @FXML
    private JFXTextField usernameTxt;

    @FXML
    private JFXPasswordField passwordTxt;

    @FXML
    private JFXPasswordField verifypasswordTxt;

    @FXML
    private Label passwordLbl;

    @FXML
    private JFXTextField firstnameTxt;

    @FXML
    private JFXTextField lastnameTxt;

    @FXML
    private Label firstnameLbl;

    @FXML
    private JFXTextField gymTxt;

    @FXML
    private JFXDatePicker bodDate;

    @FXML
    private Label usernameLbl;

    @FXML
    private Label verifypasswordLbl;

    @FXML
    private Label bodLbl;

    @FXML
    private JFXRadioButton maleGenderRdio;

    @FXML
    private ToggleGroup gender;

    @FXML
    private JFXRadioButton femaleGenderRdio;

    @FXML
    private JFXRadioButton didyouGenderRdio;

    @FXML
    private Label gymLbl;

    @FXML
    private JFXButton registerBtn;

    @FXML
    private JFXButton resetBtn;

    @FXML
    private Label statusLbl;

    @FXML
    private JFXButton backBtn;

    @FXML
    private Label lastnameLbl;

	private Connection connection;
	private DBHandler dbhandler;
	private PreparedStatement pst;


	@FXML
	public void registerAction(ActionEvent e) throws IOException, SQLException{
		String insert = "INSERT INTO trainers (TrainerUsername, TrainerGym, "
				+ "TrainerBOD, TrainerGender, TrainerFirstName, TrainerLastName, TrainerPassword)" 
				+ " VALUES (?,?,?,?,?,?,?)";
		
		statusLbl.setVisible(false);
		resetLabelsColors();
		if(!fieldsTests()) return;
		
		try {
			pst = connection.prepareStatement(insert);
			pst.setString(1, usernameTxt.getText());
			pst.setString(2, gymTxt.getText());
			pst.setDate(3, Date.valueOf(bodDate.getValue()));
			pst.setString(4, getSelectedRadio());
			pst.setString(5, firstnameTxt.getText());
			pst.setString(6, lastnameTxt.getText());
			pst.setString(7, passwordTxt.getText());
			pst.executeUpdate();
			Alert success = new Alert(Alert.AlertType.CONFIRMATION, "You have registered successfuly!", ButtonType.OK);
			success.showAndWait();
			connection.close();
			backBtnAction(e);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	private void resetLabelsColors() {
		usernameLbl.setTextFill(Paint.valueOf("#5c5757"));
		passwordLbl.setTextFill(Paint.valueOf("#5c5757"));
		verifypasswordLbl.setTextFill(Paint.valueOf("#5c5757"));
		bodLbl.setTextFill(Paint.valueOf("#5c5757"));
		gymLbl.setTextFill(Paint.valueOf("#5c5757"));
		firstnameLbl.setTextFill(Paint.valueOf("#5c5757"));
		lastnameLbl.setTextFill(Paint.valueOf("#5c5757"));
	}

	private boolean fieldsTests() {
		if(doPasswordsMatch() && allFieldsValid() && !usernameAlreadyExists()) return true;
		return false;
	}

	private boolean usernameAlreadyExists() {
		String selectDataByUsername = "SELECT * FROM trainers WHERE TrainerUsername = '" + usernameTxt.getText() + "'";
		try {
			pst = connection.prepareStatement(selectDataByUsername);
			ResultSet rs = pst.executeQuery(selectDataByUsername);
			if(rs.next()){
				usernameLbl.setTextFill(Paint.valueOf("#e83a3a"));
				statusLbl.setVisible(true);
				statusLbl.setText("This username already exists!");
				return true;
			}
			
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return false;
	}

	private boolean allFieldsValid() {
		boolean isEmpty = false;
		if(usernameTxt.getText().isEmpty()){
			usernameLbl.setTextFill(Paint.valueOf("#e83a3a"));
			isEmpty = true;
		}
		if(passwordTxt.getText().isEmpty()){
			passwordLbl.setTextFill(Paint.valueOf("#e83a3a"));
			isEmpty = true;
		}
		if(firstnameTxt.getText().isEmpty()){
			firstnameLbl.setTextFill(Paint.valueOf("#e83a3a"));
			isEmpty = true;
		}
		if(lastnameTxt.getText().isEmpty()){
			lastnameLbl.setTextFill(Paint.valueOf("#e83a3a"));
			isEmpty = true;
		}
		if(gymTxt.getText().isEmpty()){
			gymLbl.setTextFill(Paint.valueOf("#e83a3a"));
			isEmpty = true;
		}
		if(bodDate.getValue() == null){
			bodLbl.setTextFill(Paint.valueOf("#e83a3a"));
			isEmpty = true;
		}
		if(isEmpty){
			statusLbl.setVisible(true);
			statusLbl.setText("Some of the fields are empty!");
			return false;
		}
		else return true;
	}

	private boolean doPasswordsMatch() {
		if(!passwordTxt.getText().equals(verifypasswordTxt.getText())){
			statusLbl.setVisible(true);
			verifypasswordLbl.setTextFill(Paint.valueOf("#e83a3a"));
			statusLbl.setText("Verify your password!");
			return false;
		}
		else return true;
	}


	private String getSelectedRadio() {
		if (maleGenderRdio.isSelected()) return "Male";
		else if (femaleGenderRdio.isSelected()) return "Female";
		else return "None";
	}

	@FXML
	public void backBtnAction(ActionEvent e) throws IOException {
		registerBtn.getScene().getWindow().hide();
		Stage signUp = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("/FXML/LoginMain.fxml"));
		Scene scene = new Scene(root);
		signUp.setScene(scene);
		signUp.show();
	}

	@FXML
	public void resetBtnAction(ActionEvent e){
		usernameTxt.clear();
		passwordTxt.clear();
		verifypasswordTxt.clear();
		firstnameTxt.clear();
		lastnameTxt.clear();
		gymTxt.clear();
		gender.selectToggle(maleGenderRdio);
		bodDate.setValue(null);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dbhandler = new DBHandler();
		connection = dbhandler.getConnection();
	}
}
