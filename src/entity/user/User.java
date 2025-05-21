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
	private String phone;
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
	
	public User(String username, String password, String email, String fullname, String phone) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.fullname = fullname;
		this.phone = phone;
		this.ban = false;
	}
	
    public User() {
    	
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	
	
}
