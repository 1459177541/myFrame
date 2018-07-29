package dao.db.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Objects;
import java.util.Properties;

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
	    return Objects.requireNonNull(defaultConnection,"没有默认数据库链接");
    }

    public static Connection getConnection(){
	    return defaultConnection.getConn();
    }

    public GetConn(Properties properties) {
        jdbcDriver = properties.getProperty("jdbcDriver");
        dbUrl = properties.getProperty("dbUrl");
        userName = properties.getProperty("userName");
        userPassword = properties.getProperty("userPassword");
        restart();
    }

	public GetConn(String userName, String userPassword, String databaseName){
	    this.userName = userName;
	    this.userPassword = userPassword;
	    jdbcDriver = "com.mysql.jdbc.Driver";
	    dbUrl = "jdbc:mysql://localhost:3306/"+databaseName+"?useUnicode=true&characterEncoding=utf-8&useSSL=false";
	    restart();
    }

    public GetConn(String jdbcDriver, String dbUrl, String userName, String userPassword) {
        this.jdbcDriver = jdbcDriver;
        this.dbUrl = dbUrl;
        this.userName = userName;
        this.userPassword = userPassword;
        restart();
    }

    public void saveProperties(String fileName) throws IOException {
        Properties properties = new Properties(4);
        properties.setProperty("jdbcDriver",jdbcDriver);
        properties.setProperty("dbUrl",dbUrl);
        properties.setProperty("userName",userName);
        properties.setProperty("userPassword",userPassword);
        properties.store(new FileOutputStream(fileName),"jdbc config");
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

	@SuppressWarnings({"SqlNoDataSourceInspection", "SqlResolve"})
    public boolean hasTable(String name){
	    if (connection==null){
	        restart();
        }
        String sql = "SHOW TABLES;";
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()){
                String tableName = rs.getString(0);
                if (tableName.equals(name)){
                    return true;
                }
            }
        } catch (SQLException e) {
            return false;
        }
        return false;
    }

}







