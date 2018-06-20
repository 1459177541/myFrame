package util.asynchronized;

public enum ThreadState {
	WAIT("等待"), RUNNING("运行中"), COMPLETE("完成"), EXCEPTION("出错");
	
	
	private String desc;
	
	private ThreadState(String desc){
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}
	
	@Override
	public String toString() {
		return desc;
	}
	
}
