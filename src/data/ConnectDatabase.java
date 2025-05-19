package data;

import java.sql.*;
import java.util.Properties;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDatabase {
	public static Connection connect() {
		String url = "jdbc:postgresql://localhost:5432/chatbot_LLM";
		String username = "postgres";
		String password = "Dung03062002?";
		
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, username, password);
			System.out.println("Connection successfully");
		}catch(SQLException e){
			System.out.println(e);
		}
		return conn;
	}
	
	public static void main(String[] args) {
        ConnectDatabase.connect();
    }
}
