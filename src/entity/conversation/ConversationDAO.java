package entity.conversation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import data.ConnectDatabase;

public class ConversationDAO {
	private final Connection connection;
	
	public ConversationDAO() throws SQLException{
		this.connection = ConnectDatabase.connect();
	}
	
	private Conversation extractConversationFromResultSet(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        int userId = rs.getInt("user_id");
        String title = rs.getString("title");
        LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
        LocalDateTime lastModifiedAt = rs.getTimestamp("last_modified_at").toLocalDateTime();
        boolean isArchived = rs.getBoolean("is_archived");

        Conversation conv = new Conversation(id, userId, title, createdAt);
        conv.setArchived(isArchived);
        return conv;
    }
	
	public boolean insertConversation(Conversation conversation) throws SQLException{
		String sql = "INSERT INTO conversation (id, user_id, title, create_at, last_modified_at, is_archieved) VALUES (?, ?, ?, ?, ?, ?)";
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, conversation.getId());
            stmt.setInt(2, conversation.getUserId());
            stmt.setString(3, conversation.getTitle());
            stmt.setTimestamp(4, Timestamp.valueOf(conversation.getCreatedTime()));
            stmt.setTimestamp(5, Timestamp.valueOf(conversation.getLastModifiedTime()));
            stmt.setBoolean(6, conversation.isArchived());
            return stmt.executeUpdate() > 0;
        }
	}
	
	public Conversation findById(String id) throws SQLException{
		String sql = "SELECT * FROM conversation WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractConversationFromResultSet(rs);
            }
            return null;
        }
	}
	
	public List<Conversation> findByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM conversation WHERE user_id = ? ORDER BY last_modified_at DESC";
        List<Conversation> conversations = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                conversations.add(extractConversationFromResultSet(rs));
            }
        }
        return conversations;
    }
	
	public boolean updateTitle(String id, String newTitle) throws SQLException {
        String sql = "UPDATE conversation SET title = ?, last_modified_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newTitle);
            stmt.setString(2, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean archive(String id) throws SQLException {
        String sql = "UPDATE conversation SET is_archived = TRUE, last_modified_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
}
