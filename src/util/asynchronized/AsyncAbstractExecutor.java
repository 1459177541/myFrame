package util.asynchronized;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public abstract class AsyncAbstractExecutor {

    private static Map<String, AsyncAbstractExecutor> map;

    static{
        map = new HashMap<>();
        //noinspection StaticInitializerReferencesSubClass
        map.put("default",new AsyncResultExecutor<>());
    }

    public static synchronized AsyncAbstractExecutor createExecutor(String name){
        if (map.get(name)!= null){
            throw  new IllegalArgumentException();
        }else {
            AsyncAbstractExecutor asyncAbstractExecutor = new AsyncResultExecutor<>();
            map.put(name,asyncAbstractExecutor);
            return asyncAbstractExecutor;
        }
    }

    public static AsyncAbstractExecutor getExecutor(String name){
        return map.get(name);
    }

    public static AsyncAbstractExecutor getDefault(){
        return map.get("default");
    }

    private ThreadPool pool;

    protected AsyncAbstractExecutor(){
        pool = new ThreadPool();
    }

    protected AsyncLevel level = AsyncLevel.NORMAL;

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
    protected void execute(AsyncLevel level, AsyncAbstractEvent event){
        try {
            if (AsyncLevel.NOW.equals(level)){
                new Thread(event).start();
            }
            else if (AsyncLevel.NOW.equals(level) && getWaitSize() > 0) {
                new Thread(event).start();
            } else {
                if (AsyncLevel.WITHIN.equals(level) && getWaitSize()>0){
                    pool.addWithinList(event);
                }
                pool.getThreadPool().execute(event);
            }
        }catch (Exception e){
            event.setException(e);
        }
    }

    protected void execute(AsyncAbstractEvent event){
        execute(event.getAsyncLevel(),event);
    }

    /**
     * 得到等待执行的线程数目
     * @return 等待执行的线程数目
     */
    public int getWaitSize() {
        return pool.getWorkQueue().size();
    }

    /**
     * 得到已执行完成的线程数目
     * @return 已执行完成的线程数目
     */
    public long getCompleteSize() {
        return pool.getThreadPool().getCompletedTaskCount();
    }

    private class ThreadPool{
        private final ThreadPoolExecutor threadPool;
        private final PriorityBlockingQueue<Runnable> workQueue;
        private List<AsyncAbstractEvent> withinList;

        ThreadPool(){
            workQueue = new PriorityBlockingQueue<>();
            threadPool = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors()*2
                    , Runtime.getRuntime().availableProcessors()*8
                    , 8
                    , TimeUnit.HOURS
                    , workQueue);
            withinList = new LinkedList<>();
            Thread t = new Thread(this::checkWithinList);
            t.setDaemon(true);
            t.start();
        }

        private void checkWithinList() {
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

        public ThreadPoolExecutor getThreadPool() {
            return threadPool;
        }

        public PriorityBlockingQueue<Runnable> getWorkQueue() {
            return workQueue;
        }

        public void addWithinList(AsyncAbstractEvent event){
            withinList.add(event);
        }
    }

}
