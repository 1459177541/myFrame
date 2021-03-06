package asynchronous.model;

public abstract class AsyncEvent extends AsyncAbstractEvent {

	@Override
	public void run() {
		if (!ThreadState.INIT.equals(state)){
			return;
		}
		state = ThreadState.RUNNING;
		try {
			execute();
			state = ThreadState.COMPLETE;
		} catch (Exception e) {
			state = ThreadState.EXCEPTION;
			ex = e;
		}finally {
		    doShutdown();
        }
	}
	
	protected abstract void execute();
	
}
