package views.screen;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;

import entity.conversation.Conversation;
import entity.user.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.*;
import javafx.event.*;
import javafx.scene.layout.VBox;
import service.chat.ChatService;

public class MainScreenHandler implements Initializable{
	
	@FXML
	private Button newChat;
	
	@FXML
	private Button search;
	
	@FXML
	private Button viewProfile;
	
	@FXML
	private Button sendButton;
	
	@FXML
	private TextField inputMessage;
	
	@FXML
	private VBox chatContainer;
	
	@FXML
	private VBox convHistory;
	
	private User currentUser;
	private ChatService chatService;
	private Conversation conversation;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.chatService = new ChatService();
		newChat.setOnAction(this::handleNewChat);
	}
	
	private void handleNewChat(ActionEvent event) {
		String conversationId = UUID.randomUUID().toString();
//		System.out.println(conversationId);
//		currentConversation = new Conversation(
//				conversationId,
//				
//		);
	}
}
