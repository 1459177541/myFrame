package util.asynchronized;

public abstract class AsynEvent extends AsynAbstractEvent{

	@Override
	public void run() {
		state = ThreadState.RUNNING;
		try {
			execute();
			state = ThreadState.COMPLETE;
		} catch (Exception e) {
			state = ThreadState.EXCEPTION;
			ex = e;
		}
	}
	
	protected abstract void execute();
	
}
