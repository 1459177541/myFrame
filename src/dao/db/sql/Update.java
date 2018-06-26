package dao.db.sql;

import java.sql.SQLException;

import dao.db.util.GetConn;


public abstract class Update<T> extends Result<T>{

	public Update() {
		connection = GetConn.getConnection();
	}
	
	public Update(T obj) {
		this();
		this.obj = obj;
	}
	
	@Override
	public boolean execute() {
		if (!check()){
			return false;
		}
		try {
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
