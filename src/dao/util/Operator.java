package dao.util;

public enum Operator {
	LESS("<"),EQUAL("="),GRTATER(">"),NOT_LESS("<="),NOT_EQUAL("<>"),NOT_GRTATER("<="),
	AND("AND"),OR("OR"),
	ANY("ANY"),SOME("SOME"),ALL("ALL");
	private String show;
	private Operator(String show) {
		this.show = show;
	}
	@Override
	public String toString() {
		return show;
	}
}
