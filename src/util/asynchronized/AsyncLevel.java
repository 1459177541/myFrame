package util.asynchronized;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Supplier;

public enum AsyncLevel {

    IMMEDIATELY(LinkedBlockingQueue::new),
    URGENT(LinkedBlockingQueue::new),
    NORMAL(LinkedBlockingQueue::new),
    WAITABLE(LinkedBlockingQueue::new);

    private LinkedBlockingQueue<Runnable> queue;
    private AsyncLevel(Supplier<LinkedBlockingQueue> queue){
        this.queue = queue.get();
    }

    public LinkedBlockingQueue<Runnable> get(){
        return queue;
    }

}
