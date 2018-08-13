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
public class AsyncResultExecutor<T> extends AsyncAbstractExecutor {

	private AsyncResult<T> event;

	public AsyncResultExecutor(String name, AsyncResult<T> event) {
        super(name);
        this.event = event;
	}

	public AsyncResultExecutor(String name) {
        super(name);
        this.event = null;
    }

    public AsyncResultExecutor(){
	    super();
	    this.event = null;
    }
	
	@Override
	public void start() {
		start(Objects.requireNonNull(event));
	}
	
	public void start(AsyncResult<T> event) {
		execute(level,Objects.requireNonNull(event));
	}

	public T getResult() throws Exception{
		return event.getResult();
	}

	public T waitAndGetResult(){
	    return event.waitAndGetResult();
    }

	@Override
	public ThreadState getState() {
		return event.getState();
	}

}
