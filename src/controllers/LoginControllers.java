package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import DBConnection.DBHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class LoginControllers implements Initializable{

	@FXML
	private JFXButton signinBtn;

	@FXML
	private JFXTextField usernameTxt;

	@FXML
	private JFXPasswordField passwordTxt;

	@FXML
	private CheckBox rememberTgl;

	@FXML
	private Label lbl;

	@FXML
	private JFXButton registerBtn;

	@FXML
	private Label statusLbl;

	private Connection connection;
	private DBHandler dbhandler;
	private PreparedStatement pst;
	private static LoginControllers instance;

	public LoginControllers() {
		instance = this;
	}

	public static LoginControllers getInstance(){
		return instance;
	}

	public String getLoggedInTrainerUsername(){
		return usernameTxt.getText();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dbhandler = new DBHandler();
		statusLbl.setVisible(false);
	}

	@FXML
	public void registerAction(ActionEvent e) throws IOException{
		registerBtn.getScene().getWindow().hide();
		Stage signUp = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("/FXML/SignupMain.fxml"));
		Scene scene = new Scene(root);
		signUp.setScene(scene);
		signUp.show();
	}

	@FXML
	public void signinAction(ActionEvent e) throws IOException, InterruptedException{
		String selectPassword = "SELECT TrainerPassword, idTrainer FROM trainers WHERE TrainerUsername = '" + usernameTxt.getText() + "'";
		connection = dbhandler.getConnection();
		try {
			pst = connection.prepareStatement(selectPassword);
			ResultSet rs = pst.executeQuery(selectPassword);
			boolean rsEmpty = true;
			while(rs.next()){
				rsEmpty = false;
				String pass = rs.getString(1);
				if (pass.equals(passwordTxt.getText())){
					statusLbl.setVisible(true);
					statusLbl.setText("Logged In");
					statusLbl.setTextFill(Paint.valueOf("#1ab553"));
					openTrainerMainWindow(rs.getInt(2));
				}
				else{
					statusLbl.setVisible(true);
					statusLbl.setText("Wrong Username/Password");
					statusLbl.setTextFill(Paint.valueOf("#ff6060"));
				}
			}
			if(rsEmpty){
				statusLbl.setVisible(true);
				statusLbl.setText("Wrong Username/Password");
				statusLbl.setTextFill(Paint.valueOf("#ff6060"));
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	private void openTrainerMainWindow(int idTrainer) throws IOException{
		registerBtn.getScene().getWindow().hide();
		Stage trainerMain = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("/FXML/TrainerMain.fxml"));
		Scene scene = new Scene(root);
		trainerMain.setScene(scene);
		trainerMain.show();
	}
}
