package asynchronous.executor;

import asynchronous.model.AsyncResult;
import asynchronous.model.ThreadState;

import java.util.Objects;

/**
 * 封装线程池工具类,可得返回值
 * @author 杨星辰
 *
 * @Deprecated 性能浪费
 * @param <T> 返回值
 */
@Deprecated
public class AsyncResultExecutor<T> extends AsyncAbstractExecutor {

	private AsyncResult<T> event;

	private AsyncAbstractExecutor executor;

	public AsyncResultExecutor(String name, AsyncResult<T> event) {
        super(name);
        this.event = event;
	}

	public AsyncResultExecutor(String name) {
        super(name);
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
