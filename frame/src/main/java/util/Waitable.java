package util;

public interface Waitable {

    /**
     * 是否等待
     * @return 是否等待
     */
    boolean isWait();

    default void stopWait(){
        synchronized (this) {
            this.notifyAll();
        }
    }

    default void await(){
        synchronized (this) {
            while (isWait()) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
