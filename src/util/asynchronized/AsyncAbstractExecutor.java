package util.asynchronized;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public abstract class AsyncAbstractExecutor {
    private static final ThreadPoolExecutor threadPool;
    private static final PriorityBlockingQueue<Runnable> workQueue;
    private static List<AsyncAbstractEvent> withinList;

    static {
        workQueue = new PriorityBlockingQueue<>();
        threadPool = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors()*2
                , Runtime.getRuntime().availableProcessors()*8
                , 8
                , TimeUnit.HOURS
                , workQueue);
        withinList = new LinkedList<>();
        Thread t = new Thread(AsyncAbstractExecutor::checkWithinList);
        t.setDaemon(true);
        t.start();
    }

    protected AsyncLevel level = AsyncLevel.NORMAL;

    private static void checkWithinList() {
        while (true) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (threadPool) {
                withinList = withinList.stream()
                        .filter(e -> AsyncLevel.WITHIN == e.getAsyncLevel())
                        .filter(e -> ThreadState.INIT == e.getState())
                        .collect(Collectors.toList());
                withinList.removeAll(
                        withinList.stream()
                                .filter(e -> AsyncLevel.WITHIN == e.getAsyncLevel())
                                .filter(AsyncAbstractEvent::isOvertime)
                                .peek(e -> {
                                    new Thread(e).start();
                                    workQueue.remove(e);
                                })
                                .collect(Collectors.toList())
                );
            }
        }
    }

    public void setLevel(AsyncLevel level){
        this.level = level;
    }

    public abstract void start();

    public abstract ThreadState getState();

    /**
     * 调度执行
     * @param level 线程执行等级
     * @param event 线程执行事件
     */
    protected static void execute(AsyncLevel level, AsyncAbstractEvent event){
        try {
            if (AsyncLevel.NOW.equals(level)){
                new Thread(event).start();
            }
            else if (AsyncLevel.NOW.equals(level) && getWaitSize() > 0) {
                new Thread(event).start();
            } else {
                if (AsyncLevel.WITHIN.equals(level) && getWaitSize()>0){
                    withinList.add(event);
                }
                threadPool.execute(event);
            }
        }catch (Exception e){
            event.setException(e);
        }
    }

    protected static void execute(AsyncAbstractEvent event){
        execute(event.getAsyncLevel(),event);
    }

    /**
     * 得到等待执行的线程数目
     * @return 等待执行的线程数目
     */
    public static int getWaitSize() {
        return workQueue.size();
    }

    /**
     * 得到已执行完成的线程数目
     * @return 已执行完成的线程数目
     */
    public static long getCompleteSize() {
        return threadPool.getCompletedTaskCount();
    }

}
