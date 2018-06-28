package util.asynchronized;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public abstract class AsyncAbstractExecutor {
	protected static ThreadPoolExecutor threadPool;
	protected static AsyncQueue workQueue;

	static {
		workQueue = new AsyncQueue();
		threadPool = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors()*2
				, Runtime.getRuntime().availableProcessors()*8
				, 8
				, TimeUnit.HOURS
				, workQueue);
	}

	private static class AsyncQueue extends LinkedBlockingQueue<Runnable>{
		@Override
		public int size() {
			return AsyncLevel.IMMEDIATELY.get().size()
					+AsyncLevel.URGENT.get().size()
					+AsyncLevel.NORMAL.get().size()
					+AsyncLevel.WAITABLE.get().size();
		}

		@Override
		public void put(Runnable runnable) throws InterruptedException {
			AsyncLevel.NORMAL.get().put(runnable);
		}

		@Override
		public Runnable peek() {
			if (AsyncLevel.IMMEDIATELY.get().size()>0){
				return AsyncLevel.IMMEDIATELY.get().peek();
			}
			if(AsyncLevel.URGENT.get().size()>0){
				return AsyncLevel.URGENT.get().peek();
			}
			if(AsyncLevel.NORMAL.get().size()>0){
				return AsyncLevel.NORMAL.get().peek();
			}
			if(AsyncLevel.WAITABLE.get().size()>0){
				return AsyncLevel.WAITABLE.get().peek();
			}
			return null;
		}

		public void put(AsyncLevel level, Runnable runnable){
			try {
				level.get().put(runnable);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}


	}

	protected AsyncLevel level = AsyncLevel.NORMAL;

	public void setLevel(AsyncLevel level){
		this.level = level;
	}
	
	public abstract void start();

	public abstract ThreadState getState();

	protected static void execute(AsyncLevel level, AsynAbstractEvent event){
	    try {
            if (AsyncLevel.IMMEDIATELY.equals(level) && getWaitSize() > 0) {
                new Thread(event).start();
            } else {
                threadPool.execute(event);
            }
        }catch (Exception e){
	        event.setException(e);
        }
	}

	/**
	 * 得到正在执行的线程数目
	 * @return
	 */
	public static int getExecuteSize() {
		return threadPool.getPoolSize();
	}
	
	/**
	 * 得到等待执行的线程数目
	 * @return
	 */
	public static int getWaitSize() {
		return workQueue.size();
	}
	
	/**
	 * 得到已执行完成的线程数目
	 * @return
	 */
	public static long getCompleteSize() {
		return threadPool.getCompletedTaskCount();
	}
	
}
