package util.asynchronized;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Supplier;

public enum AsyncLevel {

    /**
     * 新建线程执行，适用长时间任务
     */
    NEW(-1),
    /**
     * 现在立刻执行，适用紧急任务
     */
    NOW(0),
    /**
     * 最长可等待一段时间，适用较急的任务
     * 需设置超时
     */
    WITHIN(1),
    /**
     * 一般情况，默认任务
     */
    NORMAL(2),
    /**
     * 低优先级，当没有其他等待的线程执行
     */
    WAITABLE(3);

    private int level;
    AsyncLevel(int level){
        this.level = level;
    }

    public int getLevel(){
        return level;
    }

}
