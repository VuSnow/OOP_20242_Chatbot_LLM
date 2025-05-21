package entity.message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import data.ConnectDatabase;

public class ChatMessageDAO {
	private final Connection connection;
	
	public ChatMessageDAO() throws SQLException {
		this.connection = ConnectDatabase.connect();
	}
	
	public boolean insertMessage(ChatMessage message) throws SQLException {
        String sql = "INSERT INTO message (id, conversation_id, user_id, sender, content, timestamp) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, message.getId());
            stmt.setString(2, message.getConversationId());
            stmt.setInt(3, message.getUserId());
            stmt.setString(4, message.getSender());
            stmt.setString(5, message.getContent());
            stmt.setTimestamp(6, Timestamp.valueOf(message.getTimestamp()));
            return stmt.executeUpdate() > 0;
        }
    }
	
	public List<ChatMessage> findByConversationId(String conversationId) throws SQLException {
        String sql = "SELECT * FROM message WHERE conversation_id = ? ORDER BY timestamp ASC";
        List<ChatMessage> messages = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, conversationId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                messages.add(extractMessage(rs));
            }
        }
        return messages;
    }
	
	private ChatMessage extractMessage(ResultSet rs) throws SQLException {
        return new ChatMessage(
            rs.getString("id"),
            rs.getString("conversation_id"),
            rs.getInt("user_id"),
            rs.getString("sender"),
            rs.getString("content"),
            rs.getTimestamp("timestamp").toLocalDateTime()
        );
    }
}
