package asynchronous.executor;

import asynchronous.model.AsyncResult;
import asynchronous.model.ThreadState;

import java.util.Objects;

/**
 * 封装线程池工具类,可得返回值
 * @author 杨星辰
 *
 * @param <T> 返回值
 */
public class AsyncResultExecutor<T> extends AsyncAbstractExecutor {

	private AsyncResult<T> event;


	public AsyncResultExecutor(AsyncResult<T> event) {
        super();
        this.event = event;
	}

	public AsyncResultExecutor() {
        super();
        this.event = null;
	}	
	
	@Override
	public void start() {
		start(event);
	}
	
	public void start(AsyncResult<T> event) {
		execute(level,Objects.requireNonNull(event));
	}

	public T getResult() throws Exception{
		return AsyncExecuteManage.getResult(event);
	}

	@Override
	public ThreadState getState() {
		return AsyncExecuteManage.getState(event);
	}


}
