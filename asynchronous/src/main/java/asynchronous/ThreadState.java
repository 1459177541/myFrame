package asynchronous;

public enum ThreadState {
	INIT("初始化"), RUNNING("运行中"), COMPLETE("完成"), EXCEPTION("出错"), SHUTDOWN("停止");

	private String desc;
	
	ThreadState(String desc){
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
