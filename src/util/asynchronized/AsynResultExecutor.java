package util.asynchronized;

import java.util.Objects;

/**
 * 封装线程池工具类,可得返回值
 * @author 杨星辰
 *
 * @param <T> 返回值
 */
public class AsynResultExecutor<T> extends AsyncAbstractExecutor{

	private AsynResult<T> event;


	public AsynResultExecutor(AsynResult<T> event) {
		this.event = event;
	}

	public AsynResultExecutor() {
		this.event = null;
	}	
	
	@Override
	public void start() {
		try {
			threadPool.execute(Objects.requireNonNull(event));
		} catch (Exception e) {
			event.setException(e);
		}
	}
	
	public void start(AsynResult<T> event) {
		this.event = event;
		try {
			threadPool.execute(event);
		} catch (Exception e) {
			event.setException(e);
		}
	}

	public T getResult() throws Exception{
		return AsyncStaticExecuter.getResult(event);
	}

	@Override
	public ThreadState getState() {
		return AsyncStaticExecuter.getState(event);
	}


}
