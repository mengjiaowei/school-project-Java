package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

	static Statement statement;
	static ResultSet rs;
	
	Database() throws SQLException {

		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection mysqlCon = DriverManager.getConnection("jdbc:mysql://90.231.28.63:3306/curtana?" + "user=Magnus&" + "password=j3dd4EGMXRTA9Zvu");
			statement = mysqlCon.createStatement();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
