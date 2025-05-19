package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import utils.Configs;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.*;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent Root = FXMLLoader.load(getClass().getResource(Configs.LOGIN_SCREEN_PATH));
			Scene scene = new Scene(Root);
			
			primaryStage.setTitle("Chatbot RAG");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
