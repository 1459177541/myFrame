package dao.db.criteria;

import java.util.Objects;

public class ConditionCriteria<T> extends Criteria<T> {

    private String key;

    private ConditionOperator link;

    private String valueString;
    private Number valueNumber;

    public ConditionCriteria() {
    }

    public ConditionCriteria(String key, ConditionOperator link, String valueString) {
        this.key = key;
        setLink(link);
        this.valueString = valueString;
    }

    public ConditionCriteria(String key, ConditionOperator link, Number valueNumber) {
        this.key = key;
        this.link = link;
        this.valueNumber = valueNumber;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setLink(ConditionOperator link) {
        this.link = link;
    }

    public void setValueString(String valueString) {
        this.valueString = valueString;
        this.valueNumber = null;
    }

    public void setValueNumber(Number valueNumber) {
        this.valueNumber = valueNumber;
        this.valueString = null;
    }

    @Override
    public String get() {
        return Objects.requireNonNull(key)
                +Objects.requireNonNull(link)
                +Objects.requireNonNullElseGet(valueString
                    ,()->Objects.requireNonNull(valueNumber).toString());
    }

}
