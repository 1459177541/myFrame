package dao.util;

import java.sql.*;

public class GetConn {

	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static final String DB_URL = "jdbc:mysql://localhost:3306/student?useUnicode=true&characterEncoding=utf-8&useSSL=false";

	private final static String USER_NAME = "";
	private final static String USER_PASSWORD = "";
	
	private static Connection connection = null;
	
	static {
		try {
			Class.forName(JDBC_DRIVER);
			connection = DriverManager.getConnection(DB_URL, USER_NAME, USER_PASSWORD);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() {
		return connection;
	}
	/**
	 * �ر����ݿ�����
	 * @return �ر��Ƿ�ɹ�
	 */
	public static boolean exitConnection() {
		if (null==connection) {
			return false;
		}
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static boolean restart() {
		if (null!=connection) {
			if (!exitConnection()) {
				return false;
			}
		}
		try {
			Class.forName(JDBC_DRIVER);
			connection = DriverManager.getConnection(DB_URL, USER_NAME, USER_PASSWORD);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}







