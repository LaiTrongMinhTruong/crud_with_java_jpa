package baitapdautien.connection;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
	private final String serverName = "localhost";
	private final String dbName = "banhang";
	private final String portNumber = "3306";
	private final String userID = "root";
	private final String password = "123456";

	public Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://" + serverName + ":" + portNumber + "/" + dbName
					+ "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
			conn = DriverManager.getConnection(url, userID, password);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return conn;
	}

	public static void main(String[] args) {
		try {
			Connection conn = new DBConnection().getConnection();
			if (conn != null) {
				System.out.println("Kết nối cơ sở dữ liệu thành công!");
			} else {
				System.out.println("Kết nối thất bại!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
