package entity.user;

import javafx.beans.property.*;
import javafx.collections.FXCollections;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import common.exception.InvalidEmailFormatException;
import common.exception.PasswordMismatchException;
import common.exception.UsernameAlreadyExistsException;
import data.ConnectDatabase;

public class User {
	private int id;
	private String username;
	private String password;
	private String email;
	private String fullname;
	private boolean ban;
	protected Statement stm;
	private static final Pattern EMAIL_REGEX =
            Pattern.compile("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
	
	public User(int id, String username, String password, String email, String fullname, boolean ban) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.fullname = fullname;
		this.ban = ban;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public boolean isBan() {
		return ban;
	}

	public void setBan(boolean ban) {
		this.ban = ban;
	}
	
	public User() throws SQLException{
		stm = ConnectDatabase.connect().createStatement();
	}
	
	public User authenticate(String username, String password) throws SQLException {
		String sql = "select * from \"user\" " + "where username = '" + username + "' and password = '" + password + "'";
        System.out.println(sql);
        Statement stm = null;
        ResultSet res = null;
        try {
            stm = ConnectDatabase.connect().createStatement();
            res = stm.executeQuery(sql);
            System.out.println(res);
            if (res.next()) {
                int found_id = res.getInt("id");
                String found_username = res.getString("username");
                String found_fullname = res.getString("fullname");
                String found_email = res.getString("email");
                boolean found_ban = res.getBoolean("ban");
                String found_password = res.getString("password");
                User found_user = new User(found_id, found_username, found_password, found_email, found_fullname, found_ban);
                return found_user;
            } else {
                return null;
            }
        } finally {
            if (res != null) res.close();
            if (stm != null) stm.close();
        }
	}
	
	public boolean isUsernameTaken(String username) {
	    String sql = "SELECT id FROM \"user\" WHERE username = ?";
	    try (PreparedStatement pstmt = ConnectDatabase.connect().prepareStatement(sql)) {
	        pstmt.setString(1, username);
	        ResultSet rs = pstmt.executeQuery();
	        return rs.next(); // true nếu tìm thấy username
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return true; // xử lý thận trọng: nếu có lỗi, coi như tên đã bị chiếm
	    }
	}
	
	public void validateForSignup(String password, String confirmPassword, String email) throws PasswordMismatchException, InvalidEmailFormatException, UsernameAlreadyExistsException {
        if (!password.equals(confirmPassword)) {
            throw new PasswordMismatchException();
        }
        if (!EMAIL_REGEX.matcher(email).matches()) {
            throw new InvalidEmailFormatException();
        }
        if (isUsernameTaken(this.username)) {
            throw new UsernameAlreadyExistsException();
        }
    }
	
	public boolean createUser(String username, String password, String email, String fullname) throws SQLException {
		String sql = "insert into \"user\" (username, password, email, fullname, ban) values (?, ?, ?, ?, false)";
		try (PreparedStatement pstmt = ConnectDatabase.connect().prepareStatement(sql)){
			pstmt.setString(1, username);
			pstmt.setString(2, password);
			pstmt.setString(3, email);
			pstmt.setString(4, fullname);
			int affectedRows = pstmt.executeUpdate();
			return affectedRows > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
