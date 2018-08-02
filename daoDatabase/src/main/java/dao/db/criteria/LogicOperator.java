package dao.db.criteria;

public enum LogicOperator {

    AND("AND"),OR("OR");

    private String show;
    LogicOperator(String show) {
        this.show = show;
    }
    @Override
    public String toString() {
        return show;
    }
}
