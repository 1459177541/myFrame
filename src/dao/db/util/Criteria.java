package dao.db.util;

public class Criteria<T> {
	
	private CriteriaState state;
	private CriteriaState criteriaLink;
	
	private Criteria<T> criteriaL;
	private Criteria<T> criteriaR;
	
	private Operator link;
	
	private String key;
	private Number valueNumber;
	private String valueString;
	
	private String childSelect;
	
	public Criteria(Criteria<T> criteriaL, Operator link, Criteria<T> criteriaR) {
		super();
		this.link = link;
		this.criteriaL = criteriaL;
		this.criteriaR = criteriaR;
		state = CriteriaState.LR;
	}

	public Criteria(String key, Operator link, Number valueNumber) {
		super();
		this.key = key;
		this.link = link;
		this.valueNumber = valueNumber;
		state = CriteriaState.KV;
	}

	public Criteria(String key, Operator link, String valueString) {
		super();
		this.key = key;
		this.link = link;
		this.valueString = valueString;
		state = CriteriaState.KV;
	}
	
	public Criteria(String key, Operator link, CriteriaState criteriaLink, String childSelect) {
		super();
		this.criteriaLink = criteriaLink;
		this.link = link;
		this.key = key;
		this.childSelect = childSelect;
		state = CriteriaState.CS;
	}

	public String get() {
		String L = null;
		String R = null;
		switch (state) {
		case CS:
			L = key;
			R = " "+criteriaLink+" ("+childSelect+")";
			break;
		case KV:
			L = key;
			if (null==valueNumber) {
				R = "'"+valueString+"'";
			}else {
				R = valueNumber.toString();
			}
			break;
		case LR:
			L = "("+criteriaL.toString();
			R = criteriaR.toString()+")";
			break;
		default:
			return null;	
		}
		return L+link+R;
	}
	
	@Override
	public String toString() {
		return get();
	}
	
}
