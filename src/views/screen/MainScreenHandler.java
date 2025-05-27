package views.screen;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

import org.json.JSONObject;

import client.LLMClient;
import client.LLMClientFactory;

import java.time.LocalDateTime;

import entity.conversation.Conversation;
import entity.message.ChatMessage;
import entity.message.ChatMessageType;
import entity.user.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.TextField;
import javafx.scene.*;
import javafx.event.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import service.chat.ChatService;
import utils.Configs;

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
	private Button logoutBtn;
	
	@FXML
	private TextField inputMessage;
	
	@FXML
	private VBox chatContainer;
	
	@FXML
	private VBox convHistory;
	
	@FXML
	private ComboBox<String> modelSelector;
	
	private User currentUser;
	private ChatService chatService;
	private Conversation conversation;
	private LLMClient llmClient;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		newChat.setOnAction(this::handleNewChat);
		logoutBtn.setOnAction(this::logout);
		
		// Disable nút gửi ban đầu
		sendButton.setDisable(true);
		
		inputMessage.textProperty().addListener((observable, oldValue, newValue) -> {
			sendButton.setDisable(newValue.trim().isEmpty());
		});
		
		sendButton.setOnAction(this::sendMsg);
		
		modelSelector.getItems().addAll("Gemini-2.0-flash", "GPT-4", "Local Model");
		modelSelector.setValue("Gemini-2.0-flash");
		this.setModel("Gemini-2.0-flash");
		modelSelector.setOnAction(event -> {
			String selectedModel = modelSelector.getValue();
//			System.out.println(selectedModel);
			this.setModel(selectedModel);
		});
		
	}
	
	private void setModel(String modelName) {
		this.llmClient = LLMClientFactory.getClient(modelName);
	}
	
	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	private void handleNewChat(ActionEvent event) {
		String conversationId = UUID.randomUUID().toString();
//		System.out.println(conversationId);
		this.conversation = new Conversation(
			conversationId,
			this.currentUser.getId(),
			"new chat",
			LocalDateTime.now()
		);
		chatContainer.getChildren().clear();
	}
	
	public void initWithUser(User user) {
	    this.currentUser = user;
	    try {
	        this.chatService = new ChatService(this.currentUser);
	        convHistory.getChildren().clear();
	        this.loadHistory();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	private void loadHistory() throws SQLException {
		List<Conversation> conversations = this.chatService.getConversationsForUser();
		for(Conversation c : conversations) {
        	Button btn = new Button(c.getTitle());
        	btn.setPrefWidth(convHistory.getPrefWidth());
        	btn.setPrefHeight(30);
        	btn.setMaxWidth(convHistory.getPrefWidth());
        	btn.setAlignment(Pos.CENTER_LEFT);
        	btn.setTextOverrun(OverrunStyle.ELLIPSIS);
        	btn.setWrapText(false);
        	btn.setId(c.getId());
        	btn.setOnAction(e -> this.loadConversation(btn.getId()));
        	
        	VBox.setMargin(btn, new Insets(2, 0, 2, 0)); 
        	convHistory.getChildren().add(btn);
        }
	}
	
	private void loadConversation(String conversationId) {
		try {
			this.conversation = this.chatService.getConversationById(conversationId);
			
			List<ChatMessage> messages = this.chatService.loadMessagesForConversation(conversationId);
//			System.out.println(messages);
			chatContainer.getChildren().clear();
			
			for (ChatMessage message : messages) {
	            boolean isUser = !"assistant".equalsIgnoreCase(message.getSender());
	            HBox bubble = this.buildMessageBubble(message.getContent(), isUser);
	            chatContainer.getChildren().add(bubble);
	        }
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void sendMsg(ActionEvent event) {
		// 1. Get user message
		String msg = inputMessage.getText();
//		System.out.println(inputMessage.getText().trim());
		
		// 2. If the conversation is null, create new conversation
		if (this.conversation == null) {
			this.handleNewChat(null);
		}
		
		// 3. Append user message to chatContainer (update UI)
	    chatContainer.getChildren().add(this.buildMessageBubble(msg, true));
	    
	    // 4. Clear inputMessage
	    inputMessage.clear();
	    
	    try {
	    	// 5. Check if the conversation id exist in database -> continue chat, else -> new chat
	    	if(!this.chatService.existsInDatabase(this.conversation)) {
	    		this.chatService.createConversation(conversation);
	    	}
	    	
	    	// 6. Create userMessage and store in database
	    	ChatMessage userMessage = new ChatMessage(this.conversation.getId(), this.currentUser.getId(), "user", msg, LocalDateTime.now(), ChatMessageType.TEXT);
	    	this.chatService.createMessage(userMessage);
	    	
	    	// 7. Get the history chat to provide LLM for understanding conversation
	    	List<ChatMessage> context = this.chatService.loadMessagesForConversation(this.conversation.getId());
	    	
	    	// 8. Get the response from LLM Client
	    	JSONObject response = llmClient.generateResponse(msg, context, this.conversation);
	    	String answer = response.getString("answer");
	    	String title = response.getString("title");
//	    	System.out.println(response);
	    	
	    	// 9. Append LLM Response to chatContainer (update UI)
	        chatContainer.getChildren().add(this.buildMessageBubble(answer, false));
	        
	        // 10. Save LLM Response to database
	        ChatMessage AiMessage = new ChatMessage(this.conversation.getId(), 0, "assistant", answer ,LocalDateTime.now(), ChatMessageType.TEXT);
	        this.chatService.createMessage(AiMessage);
	        
	        // 11. Update last modified time of this conversation
	        this.conversation.setLastModifiedTime(AiMessage.getTimestamp());
	        if (!title.equals(this.conversation.getTitle())) {
	        	this.conversation.setTitle(title);
	        }
	        this.chatService.updateModifiedTime(this.conversation);
	        
	        // 12. Update UI
	        convHistory.getChildren().clear();
	        this.loadHistory();
	    	
	    } catch (SQLException e) {
	    	e.printStackTrace();
	    }
	}
	
	private HBox buildMessageBubble(String content, boolean isUser) {
		Label label = new Label(content);
	    label.setWrapText(true);
	    label.setMaxWidth(400);
	    label.setMinHeight(Label.USE_PREF_SIZE);
	    label.setTextOverrun(OverrunStyle.CLIP);
	    label.setPadding(new Insets(8));
	    label.setStyle(isUser ?
	        "-fx-background-color: #C8E4FF; -fx-background-radius: 10;" :
	        "-fx-background-color: #F0F0F0; -fx-background-radius: 10;"
	    );

	    HBox wrapper = new HBox(label);
	    wrapper.setPadding(new Insets(5));
	    wrapper.setAlignment(isUser ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
	    return wrapper;
	}
	
	private void logout(ActionEvent event) {
		this.currentUser = null;
	    this.chatService = null;
	    this.conversation = null;
	    this.llmClient = null;
	    
	    // Đóng màn hình hiện tại (MainScreen)
	    Stage stage = (Stage) logoutBtn.getScene().getWindow();
	    stage.close();
	    
	    // Load màn hình login (giả sử có file LoginScreen.fxml)
	    try {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource(Configs.LOGIN_SCREEN_PATH));
	        Parent root = loader.load();
	        Stage loginStage = new Stage();
	        loginStage.setScene(new Scene(root));
	        loginStage.setTitle("Login");
	        loginStage.show();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}
