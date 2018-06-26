package util.asynchronized;

import java.util.Objects;

/**
 * 封装线程池工具类,可得返回值
 * @author 杨星辰
 *
 * @param <T> 返回值
 */
public class AsyncResultExecutor<T> extends AsyncAbstractExecutor {

	private AsynResult<T> event;


	public AsyncResultExecutor(AsynResult<T> event) {
		this.event = event;
	}

	public AsyncResultExecutor() {
		this.event = null;
	}	
	
	@Override
	public void start() {
		start(event);
	}
	
	public void start(AsynResult<T> event) {
		execute(level,Objects.requireNonNull(event));
	}

	public T getResult() throws Exception{
		return StaticAsyncExecute.getResult(event);
	}

	@Override
	public ThreadState getState() {
		return StaticAsyncExecute.getState(event);
	}


}
