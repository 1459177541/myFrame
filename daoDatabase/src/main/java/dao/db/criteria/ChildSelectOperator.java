package dao.db.criteria;

public enum ChildSelectOperator {

    ANY("ANY"),SOME("SOME"),ALL("ALL");

    private String show;
    ChildSelectOperator(String show) {
        this.show = show;
    }
    @Override
    public String toString() {
        return show;
    }
}
