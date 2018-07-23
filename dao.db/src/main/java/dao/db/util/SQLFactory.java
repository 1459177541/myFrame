package dao.db.util;

import dao.db.sql.Edit;
import dao.db.sql.Result;
import frame.Factory;

import java.util.Objects;


public class SQLFactory<T> implements Factory<DBExecute, Result<T>> {

	private T obj;
	private T oldObj;
	private Criteria<T> criteria;
	
	public T getObj() {
		return obj;
	}
	
	public SQLFactory<T> setOldObj(T obj) {
		this.oldObj = obj;
		return this;
	}

	public SQLFactory<T> setObj(T obj) {
		this.obj = obj;
		return this;
	}
	
	public SQLFactory<T> setCriteria(Criteria<T> criteria) {
		this.criteria = criteria;
		return this;
	}
	
	@Override
	public Result<T> get(DBExecute execute) {
		@SuppressWarnings("unchecked")
		Result<T> result = (Result<T>) execute.create();
		result.setObj(Objects.requireNonNull(obj));
		if(DBExecute.EDIT.equals(execute)) {
			((Edit<T>)result).setOldObj(Objects.requireNonNull(oldObj));
		}
		if (null!=criteria) {
			result.setCriteria(criteria);
		}
		return result;
	}
	
}
