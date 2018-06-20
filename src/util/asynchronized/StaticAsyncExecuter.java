package util.asynchronized;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 静态封装线程池工具类
 * @author 杨星辰
 *
 */
public class StaticAsyncExecuter extends AsynAbstractExecutor{

	private StaticAsyncExecuter() {
	}
		
	public static <E,R> AsynResult<R> startResult(E arg, Function<E,R> function){
		AsynResult<R> asynEvent = new AsynResult<R>() {
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
	
	public static <R> AsynResult<R> startResult(Supplier<R> supplier){
		AsynResult<R> asynEvent = new AsynResult<R>() {
			@Override
			protected R execute() {
				return supplier.get();
			}
		};
		try {
			threadPool.execute(asynEvent);
		} catch (Exception e) {
			asynEvent.setException(e);
		}
		return asynEvent;		
	}
	
	public static <T> AsynEvent start(T arg, Consumer<T> consumer) {
		AsynEvent event = new AsynEvent() {
			@Override
			protected void execute() {
				consumer.accept(arg);
			}
		};
		return event;
	}
	
	public static <E> ThreadState getState(AsynResult<E> event) {
		return event.getState();
	}
	
	public static <E> E getResult(AsynResult<E> event) throws Exception{
		if (!event.isCompleted()) {
			event.await();
		}
		return event.getResult();
	}

	@Override
	@Deprecated
	public void start() {
	}

	@Override
	@Deprecated
	public ThreadState getState() {
		return null;
	}
}
