package util.asynchronized;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Supplier;

public enum AsyncLevel {

    /**
     * 现在立刻执行
     */
    NOW(0),
    /**
     * 最长可等待一段时间
     */
    WITHIN(1),
    /**
     * 一般情况
     */
    NORMAL(2),
    /**
     * 低优先级
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
