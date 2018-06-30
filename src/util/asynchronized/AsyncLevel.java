package util.asynchronized;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Supplier;

public enum AsyncLevel {

    IMMEDIATELY(3),
    URGENT(2),
    NORMAL(1),
    WAITABLE(0);

    private int level;
    private AsyncLevel(int level){
        this.level = level;
    }

    public int getLevel(){
        return level;
    }

}
