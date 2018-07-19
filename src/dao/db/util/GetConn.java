package dao.db.util;

import java.sql.*;
import java.util.Objects;

public class GetConn {

	private final String jdbcDriver;
	private final String dbUrl;
	private final String userName;
	private final String userPassword;

	private Connection connection = null;

	private static GetConn defaultConnection;

	public GetConn toDefault(){
	    defaultConnection = this;
	    return this;
    }

    public static GetConn getDefault(){
	    return Objects.requireNonNull(defaultConnection);
    }

    public static Connection getConnection(){
	    return defaultConnection.getConn();
    }

	public GetConn(String userName, String userPassword){
	    this.userName = userName;
	    this.userPassword = userPassword;
	    jdbcDriver = "com.mysql.jdbc.Driver";
	    dbUrl = "jdbc:mysql://localhost:3306/student?useUnicode=true&characterEncoding=utf-8&useSSL=false";
	    restart();
    }

    public GetConn(String jdbcDriver, String dbUrl, String userName, String userPassword) {
        this.jdbcDriver = jdbcDriver;
        this.dbUrl = dbUrl;
        this.userName = userName;
        this.userPassword = userPassword;
        restart();
    }

    public Connection getConn() {
		return connection;
	}

	public boolean exitConnection() {
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
	
	public boolean restart() {
		if (null!=connection) {
			if (!exitConnection()) {
				return false;
			}
		}
		try {
			Class.forName(jdbcDriver);
			connection = DriverManager.getConnection(dbUrl, userName, userPassword);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}







