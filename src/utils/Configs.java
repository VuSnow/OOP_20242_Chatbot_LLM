package utils;

import java.time.format.DateTimeFormatter;

public class Configs {
	public static final String LOGIN_SCREEN_PATH = "/views/fxml/LoginForm.fxml";
	public static final String MAIN_SCREEN_PATH = "/views/fxml/MainScreen.fxml";
	public static final String REGISTER_SCREEN_PATH = "/views/fxml/RegisterForm.fxml";
	
	public static final DateTimeFormatter formatter_date_time = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    public static final DateTimeFormatter formatter_date = DateTimeFormatter.ofPattern("dd/MM/yyyy");
}
