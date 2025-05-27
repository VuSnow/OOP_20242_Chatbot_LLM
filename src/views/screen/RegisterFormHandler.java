package views.screen;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

import common.exception.AimsException;
import common.exception.InvalidEmailFormatException;
import common.exception.PasswordMismatchException;
import common.exception.UsernameAlreadyExistsException;
import dao.user.UserDAO;
import entity.user.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import service.user.UserService;
import utils.Configs;

public class RegisterFormHandler implements Initializable{
	
	@FXML
	private TextField username;
	
	@FXML
	private PasswordField password;
	
	@FXML
	private PasswordField confirmPassword;
	
	@FXML
	private TextField fullname;
	
	@FXML
	private TextField email;
	
	@FXML 
	private TextField phone;
	
	@FXML
	private Button registerButton;
	
	@FXML
	private Text warning;
	
	@FXML
	private Hyperlink login;
	
	private UserService userService;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		try {
			this.userService = new UserService(new UserDAO());
		} catch (SQLException e) {
			showError("Cannot connect to database");
            e.printStackTrace();
            return;
		}
		// TODO Auto-generated method stub
		registerButton.setOnAction(this::handleRegister);
		login.setOnAction(this::loginForm);
		warning.setVisible(false);
	}
	
	private void handleRegister(ActionEvent event) {
		String enteredUsername = username.getText();
		String enteredPassword = password.getText();
		String enteredConfirmPassword = confirmPassword.getText();
		String enteredFullname = fullname.getText();
		String enteredEmail = email.getText();
		String enteredPhone = phone.getText();
		
		User newUser = new User(enteredUsername, enteredPassword, enteredEmail, enteredFullname, enteredPhone);
		try {
			boolean success = this.userService.registerUser(newUser, enteredConfirmPassword);
			if (success) {
				this.navigateToLogin(event);
			} else {
				showError("Registration failed. Please try again.");
			}
		} catch (PasswordMismatchException | InvalidEmailFormatException | UsernameAlreadyExistsException e) {
            showError(e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Database error. Please try again.");
        }
	}
	
	private void loginForm(ActionEvent event) {
    	try {
            Parent signupRoot = FXMLLoader.load(getClass().getResource(Configs.LOGIN_SCREEN_PATH));
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(signupRoot));
            stage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	private void showError(String message) {
        warning.setText(message);
        warning.setVisible(true);
    }
	
	private void navigateToLogin(ActionEvent event) {
        try {
            Parent loginRoot = FXMLLoader.load(getClass().getResource(Configs.LOGIN_SCREEN_PATH));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(loginRoot));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Unable to load login screen.");
        }
    }

}
