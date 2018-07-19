package dao.util;

import util.Waitable;
import util.asynchronized.AsyncExecuteManage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public abstract class AbstractBeanBuffer<T> implements BeanBuffer<T>, Waitable {

    protected List<T> data;

    protected BeanBufferState state;

    protected final Class<T> clazz;

    protected ReadWriteLock lock;

    public AbstractBeanBuffer(Class<T> clazz){
        lock = new ReentrantReadWriteLock();
        this.clazz = clazz;
        state = BeanBufferState.INIT;
    }

    @Override
    public BeanBufferState getState(){
        return state;
    }

    /**
     * 得到数据
     * @return 包含数据的列表
     */
    public ArrayList<T> get(){
        return readSomething(null, a->new ArrayList<>(data));
    }

    @Override
    public void update(List<T> list){
        editSomething(list, l->{
            data = new ArrayList<>(list);
        });
    }

    @Override
    public int size() {
        return readSomething(null, a-> data.size());
    }

    @Override
    public boolean isEmpty() {
        return readSomething(null, a-> data.isEmpty());
    }

    @Override
    public boolean contains(Object o) {
        return readSomething(o, a-> data.contains(o));
    }

    @Override
    public Iterator<T> iterator() {
        return readSomething(null, a-> data.iterator());
    }

    @Override
    public Object[] toArray() {
        return readSomething(null, a-> data.toArray());
    }

    @Override
    public <A> A[] toArray(A[] a) {
        return readSomething(a, t-> data.toArray(t));
    }

    @Override
    public boolean add(T t) {
        return editSomething(t, a->{
            return data.add(a);
        });
    }

    @Override
    public boolean remove(Object o) {
        return editSomething(o, a->{
            return data.remove(a);
        });
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return editSomething(c, a->{
            return data.containsAll(a);
        });
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return editSomething(c, a->{
            return data.addAll(a);
        });
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return editSomething(c, a->{
            return data.retainAll(a);
        });
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return editSomething(c, a->{
            return data.retainAll(a);
        });
    }

    @Override
    public void clear() {
        editSomething(data, (Consumer<List<T>>) List::clear);
    }

    @Override
    public Stream<T> stream(){
        return readSomething(null, a-> data.stream());
    }



    @Override
    public boolean isWait() {
        return isCompleted();
    }

    @Override
    public boolean isCompleted() {
        return BeanBufferState.COMPLETE == state;
    }

    /**
     * 遍历列表，将每个元素新建线程执行更新方法
     * @param list 待更新的缓存列表
     */
    @Override
    public void synchronization(List<BeanBuffer<T>> list){
        state = BeanBufferState.SYNCHRONIZATION;
        list.forEach(e -> AsyncExecuteManage.start(()-> readSomething(e, beanBuffer -> {
            beanBuffer.update(data);
            return null;
        })));
        state = BeanBufferState.COMPLETE;
    }

    protected  <A, R> R editSomething(A arg, Function<A, R> function){
        await();
        state = BeanBufferState.EDITING;
        R r;
        lock.writeLock().lock();
        try {
            r = function.apply(arg);
        }finally {
            lock.writeLock().unlock();
            state = BeanBufferState.COMPLETE;
        }
        return r;
    }

    protected  <A> void editSomething(A arg, Consumer<A> consumer){
        await();
        state = BeanBufferState.EDITING;
        lock.writeLock().lock();
        try {
            consumer.accept(arg);
        }finally {
            lock.writeLock().unlock();
            state = BeanBufferState.COMPLETE;
        }
    }

    protected  <A,R> R readSomething(A arg, Function<A, R> function){
        await();
        R r;
        lock.readLock().lock();
        try {
            r = function.apply(arg);
        }finally {
            lock.readLock().unlock();
        }
        return r;
    }

}
