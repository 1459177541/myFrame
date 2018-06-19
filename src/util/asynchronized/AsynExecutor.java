package util.asynchronized;

import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * 封装线程池工具类
 * @author 杨星辰
 *
 * @param <T> 返回值
 */
public class AsynExecutor<T>{

	private AsynAbstractResult<T> event;
	private static ThreadPoolExecutor threadPool;
	private static LinkedBlockingQueue<Runnable> workQueue;

	static {
		workQueue = new LinkedBlockingQueue<>();
		threadPool = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors()*2
				, Runtime.getRuntime().availableProcessors()*8
				, 8
				, TimeUnit.HOURS
				, workQueue);
	}

	public AsynExecutor(AsynAbstractResult<T> event) {
		this.event = event;
	}

	public AsynExecutor() {
		this.event = null;
	}	
	
	public void start() {
		try {
			threadPool.execute(Objects.requireNonNull(event));
		} catch (Exception e) {
			event.setException(e);
		}
	}
	
	public void start(AsynAbstractResult<T> event) {
		this.event = event;
		try {
			threadPool.execute(event);
		} catch (Exception e) {
			event.setException(e);
		}
	}
	
	public static <E,R> AsynAbstractResult<R> start(E arg, Function<E,R> function){
		AsynAbstractResult<R> asynEvent = new AsynAbstractResult<R>() {
			@Override
			protected R execute() {
				return function.apply(arg);
			}
		};
		try {
			threadPool.execute(asynEvent);
		} catch (Exception e) {
			asynEvent.setException(e);
		}
		return asynEvent;
	}

	public T getResult() throws Exception{
		return AsynExecutor.getResult(event);
	}
	
	public static <E> E getResult(AsynAbstractResult<E> event) throws Exception{
		if (!event.isCompleted()) {
			event.await();
		}
		return event.getResult();
	}

	public ThreadState getState() {
		return AsynExecutor.getState(event);
	}
	
	public static <E> ThreadState getState(AsynAbstractResult<E> event) {
		return event.getState();
	}

}
