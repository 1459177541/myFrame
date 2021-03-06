package asynchronous.executor;

import asynchronous.model.AsyncEvent;
import asynchronous.model.AsyncResult;
import asynchronous.model.ThreadState;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 静态封装线程池工具类
 * @author 杨星辰
 *
 */
public class AsyncExecuteManage{

    private static AsyncAbstractExecutor asyncAbstractExecutor;

    static {
        asyncAbstractExecutor = AsyncAbstractExecutor.getDefault();
    }

	private AsyncExecuteManage() {
	}

	/**
	 * 执行带有返回值的线程
	 * @param arg 参数
	 * @param function 方法体
	 * @return 执行状态对象
	 */
	public static <E,R> AsyncResult<R> startResult(E arg, Function<E,R> function){
		return startResult(AsyncLevel.NORMAL, arg, function);
	}

	/**
	 * 执行带有返回值的线程
	 * @param level 优先级
	 * @param arg 参数
	 * @param function 方法体
	 * @return 执行状态对象
	 */
	public static <E,R> AsyncResult<R> startResult(AsyncLevel level, E arg, Function<E,R> function){
		Objects.requireNonNull(function);
		AsyncResult<R> asyncEvent = new AsyncResult<>() {
			@Override
			protected R execute() {
				return function.apply(arg);
			}
		};
		asyncEvent.setAsyncLevel(level);
        asyncAbstractExecutor.execute(level,asyncEvent);
		return asyncEvent;
	}

	/**
	 * 执行带有返回值的线程
	 * @param supplier 方法体
	 * @return 执行状态对象
	 */
	public static <R> AsyncResult<R> startResult(Supplier<R> supplier){
		return startResult(AsyncLevel.NORMAL, supplier);
	}

	/**
	 * 执行带有返回值的线程
	 * @param level 优先级
	 * @param supplier 方法体
	 * @return 执行状态对象
	 */
	public static <R> AsyncResult<R> startResult(AsyncLevel level, Supplier<R> supplier){
		Objects.requireNonNull(supplier);
		AsyncResult<R> asyncEvent = new AsyncResult<>() {
			@Override
			protected R execute() {
				return supplier.get();
			}
		};
		asyncEvent.setAsyncLevel(level);
        asyncAbstractExecutor.execute(level,asyncEvent);
		return asyncEvent;
	}

	/**
	 * 执行新的线程
	 * @param arg 参数
	 * @param consumer 方法体
	 * @return 执行状态对象
	 */
	public static <T> AsyncEvent start(T arg, Consumer<T> consumer){
		return start(AsyncLevel.NORMAL, arg, consumer);
	}

	/**
	 * 执行新的线程
	 * @param level 优先级
	 * @param arg 参数
	 * @param consumer 方法体
	 * @return 执行状态对象
	 */
	public static <T> AsyncEvent start(AsyncLevel level, T arg, Consumer<T> consumer) {
		Objects.requireNonNull(consumer);
		AsyncEvent event = new AsyncEvent() {
			@Override
			protected void execute() {
				consumer.accept(arg);
			}
		};
		event.setAsyncLevel(level);
        asyncAbstractExecutor.execute(level,event);
		return event;
	}
	
	/**
	 * 执行新的线程
	 * @param execute 方法体
	 * @return 执行的状态对象
	 */
	public static AsyncEvent start(Execute execute){
		return start(AsyncLevel.NORMAL, execute);
	}

	/**
	 * 执行新的线程
	 * @param level 优先级
	 * @param execute 方法体
	 * @return 执行的状态对象
	 */
	public static AsyncEvent start(AsyncLevel level, Execute execute) {
		Objects.requireNonNull(execute);
		AsyncEvent event = new AsyncEvent() {
			@Override
			protected void execute() {
				execute.execute();
			}
		};
		event.setAsyncLevel(level);
        asyncAbstractExecutor.execute(level,event);
		return event;
		
	}

	/**
	 * 得到当前状态
	 * @param event 执行状态对象
	 * @return 目标线程状态
	 */
	public static <E> ThreadState getState(AsyncResult<E> event) {
		return event.getState();
	}

	/**
	 * 得到线程执行结果
	 * @param event 执行状态对象
	 * @return 结果
	 */
	public static <E> E getResult(AsyncResult<E> event) throws Exception {
		return event.getResult();
	}

	public static <E> E waitAndGetResult(AsyncResult<E> event){
	    return event.waitAndGetResult();
    }

}
