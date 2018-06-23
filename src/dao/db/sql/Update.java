package dao.db.sql;

import java.sql.SQLException;

import dao.util.GetConn;


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
		if (null==connection||null==obj) {
			return false;
		}
		if (null==sql) {
			if (null==(sql=getSql())) {
				return false;
			}
		}
		if (null==statement) {
			try {
				statement = connection.createStatement();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
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
