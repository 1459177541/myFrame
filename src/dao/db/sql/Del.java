package dao.db.sql;

public class Del<T> extends Update<T>{
	
	public Del() {
		super();
	}

	public Del(T obj) {
		super(obj);
	}

	@Override
	protected String getSql() {
		if (null==obj) {
			return null;
		}
		StringBuffer sql = new StringBuffer("DELETE FROM "+getTableName());
		if (null==criteria) {
			sql.append(" WHERE "+criteria.toString());
		}
		else {
			sql.append(get("WHERE",obj,"AND")+";");
		}
		this.sql = sql.toString();
		return this.sql;
	}

}
