package views.screen;

import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

import common.exception.AuthenticateException;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import service.user.UserService;
import utils.Configs;

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

    private UserService userService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            this.userService = new UserService(new UserDAO());
        } catch (SQLException e) {
            showError("Cannot connect to database");
            e.printStackTrace();
            return;
        }

        loginButton.setOnAction(this::handleLogin);
        signup_btn.setOnAction(this::registerForm);
        exit.setOnAction(this::handleExit);

        warning.setVisible(false);
    }

    private void handleLogin(ActionEvent event) {
        String enteredUsername = username.getText();
        String enteredPassword = password.getText();

        if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
            showError("The username or password cannot be empty");
            return;
        }

        try {
            User user = this.userService.authenticate(enteredUsername, enteredPassword);
            this.redirectToMainScreen(event, user);
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Database error. Please try again later.");
        } catch (AuthenticateException e) {
//        	e.printStackTrace();
        	showError(e.getMessage());
        } catch (IllegalArgumentException e) {
        	showError(e.getMessage());
        }
    }

    private void registerForm(ActionEvent event) {
        try {
            Parent signupRoot = FXMLLoader.load(getClass().getResource(Configs.REGISTER_SCREEN_PATH));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(signupRoot));
            stage.show();
        } catch (Exception e) {	
            e.printStackTrace();
            showError("Cannot load registration screen");
        }
    }

    private void handleExit(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void redirectToMainScreen(ActionEvent event, User currentUser) {
        try {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource(Configs.MAIN_SCREEN_PATH));
            Parent mainRoot = loader.load();

            MainScreenHandler mainController = loader.getController();
            mainController.initWithUser(currentUser);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(mainRoot));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Failed to load main screen");
        }
    }

    private void showError(String message) {
        warning.setText(message);
        warning.setVisible(true);
    }

}
