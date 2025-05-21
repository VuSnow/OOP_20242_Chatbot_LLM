package entity.user;

import entity.user.User;
import java.sql.*;

import data.ConnectDatabase;

public class UserDAO {
	private final Connection connection;
	
	public UserDAO() throws SQLException {
        this.connection = ConnectDatabase.connect();
    }
	
	private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String username = rs.getString("username");
        String password = rs.getString("password");
        String email = rs.getString("email");
        String fullname = rs.getString("fullname");
        boolean ban = rs.getBoolean("ban");
        return new User(id, username, password, email, fullname, ban);
    }
	
	public User findByUsernameAndPassword(String username, String password) throws SQLException {
        String sql = "SELECT * FROM \"user\" WHERE username = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractUserFromResultSet(rs);
            } else {
                return null;
            }
        }
    }
	
	public boolean findByUsername(String username) throws SQLException{
		String sql = "SELECT * FROM \"user\" WHERE username = ?";
		try (PreparedStatement stmt = connection.prepareStatement(sql)){
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return true;
			}else {
				return false;
			}
		}
	}
	
	public boolean insert(User user) throws SQLException {
        String sql = "INSERT INTO \"user\" (username, password, email, fullname, ban) VALUES (?, ?, ?, ?, false)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getFullname());
            return stmt.executeUpdate() > 0;
        }
    }
}
