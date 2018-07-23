package dao.db.util;

import java.util.function.Supplier;

import dao.db.sql.*;

public enum DBExecute {
	ADD(Add::new),
	DELETE(Del::new),
	EDIT(Edit::new),
	SELECT(Select::new),
	CREATE_TABLE(CreateTable::new),
	DELETE_TABLE(DelTable::new);
	
	private Supplier<Result<?>> result;
	
	DBExecute(Supplier<Result<?>> result) {
		this.result = result;
	}
	
	public Result<?> create(){
		return result.get();
	}
}
