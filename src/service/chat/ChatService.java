package service.chat;
import entity.conversation.Conversation;
import entity.message.ChatMessage;
//import util.LLMClient;
import entity.user.User;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


import dao.conversation.ConversationDAO;
import dao.message.ChatMessageDAO;
import data.ConnectDatabase;

public class ChatService {
    private final ConversationDAO conversationDAO;
    private final ChatMessageDAO messageDAO;
//    private final LLMClient llmClient;
    private final User currentUser;
    private final Connection connection;

    public ChatService(User currentUser) throws SQLException {
        this.currentUser = currentUser;
        this.conversationDAO = new ConversationDAO();
        this.messageDAO = new ChatMessageDAO();
        this.connection = ConnectDatabase.connect();
//        this.llmClient = new LLMClient(); // mock
    }

    public boolean existsInDatabase(Conversation conversation) throws SQLException {
        return conversationDAO.findById(conversation.getId()) != null;
    }

    public void createConversation(Conversation conversation) throws SQLException {
        conversationDAO.insertConversation(conversation);
    }
    
    public Conversation getConversationById(String conversationId) throws SQLException{
    	return conversationDAO.findById(conversationId);
    }
    
    public void createMessage(ChatMessage message) throws SQLException{
    	messageDAO.insertMessage(message);
    }

    public List<ChatMessage> loadMessagesForConversation(String conversationId) throws SQLException {
        return messageDAO.findByConversationId(conversationId);
    }

    public List<Conversation> getConversationsForUser() throws SQLException {
        return this.conversationDAO.findByUserId(this.currentUser.getId());
    }
    
    public void updateModifiedTime(Conversation conversation) throws SQLException {
    	this.conversationDAO.updateConversation(conversation);
    }
    
}
