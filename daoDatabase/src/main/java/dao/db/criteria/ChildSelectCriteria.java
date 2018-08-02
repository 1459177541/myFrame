package dao.db.criteria;

import java.util.Objects;

public class ChildSelectCriteria<T> extends Criteria<T> {

    private String key;

    private ConditionOperator link;

    private ChildSelectOperator criteriaState;

    private String selectSQL;

    public ChildSelectCriteria(String key, ConditionOperator link, ChildSelectOperator criteriaState, String selectSQL) {
        this.key = key;
        this.link = link;
        this.criteriaState = criteriaState;
        this.selectSQL = selectSQL;
    }

    public ChildSelectCriteria() {
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setLink(ConditionOperator link) {
        this.link = link;
    }

    public void setCriteriaState(ChildSelectOperator criteriaState) {
        this.criteriaState = criteriaState;
    }

    public void setSelectSQL(String selectSQL) {
        this.selectSQL = selectSQL;
    }

    @Override
    public String get() {
        return Objects.requireNonNull(key)
                +Objects.requireNonNull(link)
                +" "+Objects.requireNonNull(criteriaState)
                +" ("+Objects.requireNonNull(selectSQL)+")";
    }
}
