package dao.db.annotation;

public enum DB_columnType {
	TINYINT,SMALLINT,MEDIUMINT,INT,BIGINT,
	FLOAT,DOUBLE,
	DATE,TIME,YEAR,DATETIME,TIMESTAMP,
	CHAR,VARCHAR,TINYTEXT,TEXT,MEDIUMTEXT,LONGTEXT,
	TINTBLOB,BLOB,MEDIUMBLOB,LONGBLOB;
	@Override
	public String toString() {
		return super.toString();
	}
}
