package dao.db.criteria;

import java.util.Objects;

public class LogicCriteria<T> extends Criteria<T>{

    private Criteria<T> L;

    private LogicOperator link;

    private Criteria<T> R;

    public LogicCriteria(Criteria<T> l, LogicOperator link, Criteria<T> r) {
        this.L = l;
        this.link = link;
        this.R = r;
    }

    public LogicCriteria() {
    }

    public void setL(Criteria<T> l) {
        L = l;
    }

    public void setLink(LogicOperator link) {
        this.link = link;
    }

    public void setR(Criteria<T> r) {
        R = r;
    }

    @Override
    public String get() {
        return "("+Objects.requireNonNull(L).get()
                +Objects.requireNonNull(link).toString()
                +Objects.requireNonNull(R).get()+")";
    }
}
