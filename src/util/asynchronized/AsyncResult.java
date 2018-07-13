package util.asynchronized;

import util.Waitable;

import java.util.Optional;

/**
 * 可返回结果的线程类
 * @author 杨星辰
 *
 * @param <T> 返回值
 */
public abstract class AsyncResult<T> extends AsyncAbstractEvent implements Waitable {
	
	private Optional<T> optional = Optional.empty();
	
	@Override
	public void run() {
		state = ThreadState.RUNNING;
		try {
			optional = Optional.of(execute());
			state = ThreadState.COMPLETE;
		} catch (Exception e) {
			state = ThreadState.EXCEPTION;
		}
		stopWait();
	}
	
	protected abstract T execute();

	public T getResult() throws Exception {
		if (isCompleted()) {
			return optional.get();
		}else if (ThreadState.RUNNING.equals(state)) {
	        throw new IllegalStateException("未完成");
		}else {
			throw ex;
		}
	}
	
	public T waitAndGetResult() throws Exception {
		if(!isCompleted()) {
            await();
		}
		return optional.get();
	}
	
	public Optional<T> getOptional(){
		return optional;
	}

    @Override
    public boolean isWait() {
        return !isCompleted();
    }
}
