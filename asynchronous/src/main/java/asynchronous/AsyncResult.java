package asynchronous;

import util.Waitable;

import java.util.Optional;

/**
 * 可返回结果的线程类
 * @author 杨星辰
 *
 * @param <T> 返回值
 */
public abstract class AsyncResult<T> extends AsyncAbstractEvent implements Waitable {
	
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private Optional<T> result = Optional.empty();
	
	@Override
	public void run() {
		if (!ThreadState.INIT.equals(state)){
			return;
		}
		state = ThreadState.RUNNING;
		try {
			result = Optional.of(execute());
			state = ThreadState.COMPLETE;
		} catch (Exception e) {
			state = ThreadState.EXCEPTION;
		}finally {
            doShutdown();
            stopWait();
        }
	}
	
	protected abstract T execute();

	public T getResult() throws Exception {
		if (isCompleted()) {
			return result.get();
		}else if (ThreadState.RUNNING.equals(state)) {
	        throw new IllegalStateException("未完成");
		}else {
			throw ex;
		}
	}
	
	public T waitAndGetResult() {
		if(!isCompleted()) {
            await();
		}
		return result.get();
	}
	
	public Optional<T> getOptional(){
		return result;
	}

    @Override
    public boolean isWait() {
        return !isCompleted();
    }
}
