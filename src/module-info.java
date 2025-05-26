module Chatbot_RAG_LLM {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.sql;
	requires javafx.graphics;
	requires org.json;
	
	opens application to javafx.graphics, javafx.fxml;
	opens views.screen;
	exports views.screen;
	exports application;
}
