package dao.db.util;


import dao.db.sql.Edit;
import dao.db.sql.Result;
import dao.db.sql.Select;
import factory.Factory;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.FutureTask;

public class SQLFactory<T> implements Factory<DBExecute, FutureTask<ArrayList<T>>> {

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
		this.obj = Objects.requireNonNull(obj);
		return this;
	}
	
	public SQLFactory<T> setCriteria(Criteria<T> criteria) {
		this.criteria = criteria;
		return this;
	}
	
	@SuppressWarnings({"Convert2MethodRef", "unchecked"})
    @Override
	public FutureTask<ArrayList<T>> get(DBExecute execute) {
		Result<T> result = (Result<T>) execute.create();
		result.setObj(Objects.requireNonNull(obj));
		if(DBExecute.EDIT.equals(execute)) {
			((Edit<T>)result).setOldObj(Objects.requireNonNull(oldObj));
		}
		if (null!=criteria) {
			result.setCriteria(criteria);
		}
		if (DBExecute.SELECT.equals(execute)){
		    return new FutureTask<>(()->((Select)result).getResult());
        }
        return new FutureTask<>(()->result.execute(),new ArrayList<>());
	}
	
}
