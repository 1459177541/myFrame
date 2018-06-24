package util.asynchronized;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 静态封装线程池工具类
 * @author 杨星辰
 *
 */
public class AsyncStaticExecuter extends AsynAbstractExecutor{

	private AsyncStaticExecuter() {
	}
	
	/**
	 * 执行带有返回值的线程
	 * @param arg 参数
	 * @param function 方法体
	 * @return 执行状态对象
	 */
	public static <E,R> AsynResult<R> startResult(E arg, Function<E,R> function){
		Objects.requireNonNull(function);
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

	/**
	 * 执行带有返回值的线程
	 * @param supplier 方法体
	 * @return 执行状态对象
	 */
	public static <R> AsynResult<R> startResult(Supplier<R> supplier){
		Objects.requireNonNull(supplier);
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

	/**
	 * 执行新的线程
	 * @param arg 参数
	 * @param consumer 方法体
	 * @return 执行状态对象
	 */
	public static <T> AsynEvent start(T arg, Consumer<T> consumer) {
		Objects.requireNonNull(consumer);
		AsynEvent event = new AsynEvent() {
			@Override
			protected void execute() {
				consumer.accept(arg);
			}
		};
		try {
			threadPool.execute(event);
		} catch (Exception e) {
			event.setException(e);
		}
		return event;
	}
	
	/**
	 * 执行新的线程
	 * @param execute 方法体
	 * @return 执行的状态对象
	 */
	public static AsynEvent start(Execute execute) {
		Objects.requireNonNull(execute);
		AsynEvent event = new AsynEvent() {
			@Override
			protected void execute() {
				execute.execute();
			}
		};
		try {
			threadPool.execute(event);
		} catch (Exception e) {
			event.setException(e);
		}
		return event;
		
	}

	/**
	 * 得到当前状态
	 * @param event 执行状态对象
	 * @return 目标线程状态
	 */
	public static <E> ThreadState getState(AsynResult<E> event) {
		return event.getState();
	}

	/**
	 * 得到线程执行结果
	 * @param event 执行状态对象
	 * @return 结果
	 * @throws Exception 执行过程发生的错误
	 */
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
