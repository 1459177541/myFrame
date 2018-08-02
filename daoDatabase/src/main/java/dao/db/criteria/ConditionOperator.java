package dao.db.criteria;

public enum ConditionOperator {

	LESS("<"),EQUAL("="),GRTATER(">"),NOT_LESS("<="),NOT_EQUAL("<>"),NOT_GRTATER("<=");

	private String show;
	ConditionOperator(String show) {
		this.show = show;
	}
	@Override
	public String toString() {
		return show;
	}
}
