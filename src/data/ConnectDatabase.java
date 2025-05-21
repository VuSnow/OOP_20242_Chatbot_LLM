package data;

import java.sql.*;
import java.util.Properties;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDatabase {
	private static final String url = "jdbc:postgresql://localhost:5432/chatbot_LLM";
	private static final String username = "postgres";
	private static final String password = "Dung03062002?";
	
	static {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("PostgreSQL JDBC Driver not found", e);
		}
	}
	
	public static Connection connect() throws SQLException {
//		System.out.println("Connect succesfully");
        return DriverManager.getConnection(url, username, password);
    }
	
	public static void main(String[] args) throws SQLException {
        ConnectDatabase.connect();
    }
}
