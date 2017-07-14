package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
//import org.postgresql.Driver;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBconn {

	Connection conn = null;

	public DBconn() {

	}
	
	//open connection to send or receive data from DB
	public Connection Open() {

		String url = "jdbc:postgresql://localhost/DB4csv";
		String user = "postgres";
		String password = "";

		try {
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection(url, user, password);
			System.out.println("Opened database successfully");
			
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		return conn;
	}
	
	//close connection method
	public void Close() {
		try {
			
			if (conn != null) {
				conn.close();
			}
			System.out.println("Closed database successfully");

		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			
		}

	}

}
