package util.asynchronized;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Supplier;

public enum AsyncLevel {

    IMMEDIATELY(0),
    URGENT(1),
    NORMAL(2),
    WAITABLE(3);

    private int level;
    AsyncLevel(int level){
        this.level = level;
    }

    public int getLevel(){
        return level;
    }

}
