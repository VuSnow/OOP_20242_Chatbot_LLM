package views.screen;

import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

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
import utils.Configs;
import entity.user.*;

public class LoginFormHandler implements Initializable {

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Button loginButton;

    @FXML
    private Button exit;
    
    @FXML
    private Text warning;
    
    @FXML
    private Hyperlink signup_btn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Gắn sự kiện cho nút login
        loginButton.setOnAction(this::handleLogin);
        signup_btn.setOnAction(this::registerForm);
        
        // Gắn sự kiện cho nút exit
        exit.setOnAction(event -> {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        });
        
        warning.setVisible(false);
    }

    private void handleLogin(ActionEvent event) {
        String enteredUsername = username.getText();
        String enteredPassword = password.getText();

        if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
            warning.setText("The username or password cannot be empty");
            warning.setVisible(true);
            return;
        }
        
        warning.setVisible(false);
        try {
        	User user = new User().authenticate(enteredUsername, enteredPassword);
        	if (Objects.isNull(user)) {
        		warning.setText("Wrong username or password");
                warning.setVisible(true);
        	}else {
        		warning.setVisible(false);
                try {
                	Parent mainScreen = FXMLLoader.load(getClass().getResource(Configs.MAIN_SCREEN_PATH));
                	
                	Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                	stage.setScene(new Scene(mainScreen));
                	stage.show();
                } catch (Exception e) {
                	e.printStackTrace();
                }
        	}
        }catch(SQLException e) {
        	System.out.println(e);
        }
    }
    
    private void registerForm(ActionEvent event) {
    	try {
            // Load màn hình đăng ký (SignUp.fxml)
            Parent signupRoot = FXMLLoader.load(getClass().getResource(Configs.REGISTER_SCREEN_PATH));
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(signupRoot));
            stage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}