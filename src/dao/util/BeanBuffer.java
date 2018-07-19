package dao.util;

import java.util.Collection;
import java.util.List;

public interface BeanBuffer<T> extends Collection<T> {

    BeanBufferState getState();

    boolean isCompleted();

    void synchronization(List<BeanBuffer<T>> list);

    void update(List<T> list);

}
