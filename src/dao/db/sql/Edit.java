package dao.db.sql;

public class Edit<T> extends Update<T> {

	private T oldObj;
	
	public Edit() {
		super();
	}

	public Edit(T obj,T oldObj) {
		super(obj);
		this.oldObj = oldObj;
	}

	public void setOldObj(T oldObj) {
		this.oldObj = oldObj;
	}

	@Override
	protected String getSql() {
		if (null==obj) {
			return null;
		}
		StringBuffer sql = new StringBuffer("UPDATE "+getTableName()+get("SET", obj, ","));
		if (null==criteria) {
			sql.append(" WHERE "+criteria.toString());
		}
		else {
			sql.append(get("WHERE",oldObj,"AND")+";");
		}
		this.sql = sql.toString();
		return this.sql;
	}

}
