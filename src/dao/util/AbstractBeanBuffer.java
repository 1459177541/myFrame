package dao.util;

import util.Waitable;
import util.asynchronized.AsyncExecuteManage;
import util.asynchronized.AsyncLevel;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractBeanBuffer<T> implements BeanBuffer<T>, Waitable {

    protected List<T> data;

    protected volatile BeanBufferState state;

    protected final Class<T> clazz;

    protected ReadWriteLock lock;

    public AbstractBeanBuffer(Class<T> clazz){
        lock = new ReentrantReadWriteLock();
        this.clazz = clazz;
        actionStack = new Stack<>();
        undoStack = new Stack<>();
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

    @SuppressWarnings("SuspiciousToArrayCall")
    @Override
    public <A> A[] toArray(A[] a) {
        return readSomething(a, t-> data.toArray(t));
    }

    @Override
    public boolean add(T t) {
        return editSomething(t, a->{
            boolean isSuccess = data.add(a);
            if (isSuccess) {
                BeanAction<T> action = new BeanAction<>(BeanAction.ACTION_ADD);
                action.setBefore(a);
                actionStack.push(action);
            }
            return isSuccess;
        });
    }

    @Override
    public boolean remove(Object o) {
        return editSomething(o, a->{
            boolean isSuccess = data.remove(a);
            if (isSuccess) {
                BeanAction<T> action = new BeanAction<>(BeanAction.ACTION_DEL);
                //noinspection unchecked
                action.setBefore((T) a);
                actionStack.push(action);
            }
            return isSuccess;
        });
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return readSomething(c, a-> data.containsAll(a));
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return editSomething(c, a->{
            boolean isSuccess = data.addAll(a);
            if (isSuccess) {
                BeanAction<T> action = new BeanAction<>(BeanAction.ACTION_ADD);
                //noinspection unchecked
                action.setBefore((Collection<T>) a);
                actionStack.push(action);
            }
            return isSuccess;
        });
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return editSomething(c, a->{
            boolean isSuccess = data.removeAll(a);
            if (isSuccess) {
                BeanAction<T> action = new BeanAction<>(BeanAction.ACTION_DEL);
                //noinspection unchecked
                action.setBefore((Collection<T>) a);
                actionStack.push(action);
            }
            return isSuccess;
        });
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return editSomething(c, a->{
            boolean isSuccess = data.retainAll(a);
            if (isSuccess) {
                BeanAction<T> action = new BeanAction<>(BeanAction.ACTION_DEL);
                action.setBefore(data.stream().filter(d->!a.contains(d)).collect(Collectors.toList()));
                actionStack.push(action);
            }
            return isSuccess;
        });
    }

    @Override
    public void clear() {
        editSomething(data, a->{
            BeanAction<T> action = new BeanAction<>(BeanAction.ACTION_DEL);
            action.setBefore(new ArrayList<>(data));
            actionStack.push(action);
            data.clear();
        });
    }

    @Override
    public void edit(T before, T after) {
        editSomething(null, a->{
            data.set(data.indexOf(before),after);
            BeanAction<T> action = new BeanAction<>(BeanAction.ACTION_EDIT);
            action.setBefore(before);
            action.setAfter(after);
            actionStack.push(action);
        });
    }

    @SuppressWarnings("Convert2MethodRef")
    @Override
    public void forEach(Consumer<? super T> action) {
        readSomething(null,a->{
            data.forEach(e->action.accept(e));
            return null;
        });
    }

    @Override
    public Stream<T> stream(){
        return readSomething(null, a-> data.stream());
    }

    @Override
    public String toString(){
        return data.toString();
    }

    @Override
    public boolean removeIf(Predicate<? super T> filter) {
        return editSomething(filter, a->{
            boolean isSuccess;
            BeanAction<T> action = new BeanAction<>(BeanAction.ACTION_DEL);
            action.setBefore(data.stream().filter(filter).collect(Collectors.toList()));
            isSuccess = data.removeIf(filter);
            if (isSuccess){
                actionStack.push(action);
            }
            return isSuccess;
        });
    }

    @Override
    public Spliterator<T> spliterator() {
        return readSomething(null,a->data.spliterator());
    }

    @Override
    public Stream<T> parallelStream() {
        return readSomething(null,a->data.parallelStream());
    }

    @Override
    public boolean isWait() {
        return !isCompleted();
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
        await();
        state = BeanBufferState.SYNCHRONIZATION;
        list.forEach(e -> AsyncExecuteManage.start(AsyncLevel.SYSTEM, ()-> readSomething(e, beanBuffer -> {
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
            stopWait();
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
            stopWait();
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
            stopWait();
        }
        return r;
    }


    protected Stack<BeanAction<T>> actionStack;

    protected Stack<BeanAction<T>> undoStack;

    @Override
    @SuppressWarnings({"unchecked", "Duplicates"})
    public void undo() {
        if (!isUndo()){
            return;
        }
        BeanAction<T> beanAction = actionStack.pop();
        undoStack.push(beanAction);
        if (beanAction.getAction() == BeanAction.ACTION_ADD){
            editSomething(beanAction.getBefore(), b-> {
                return data.removeAll(b);
            });
        }else if (beanAction.getAction() == BeanAction.ACTION_DEL){
            editSomething(beanAction.getBefore(), b->{
                return data.addAll(b);
            });
        }else if (beanAction.getAction() == BeanAction.ACTION_EDIT){
            final T before = (T) beanAction.getBefore().toArray()[0];
            final T after = (T) beanAction.getAfter().toArray()[0];
            editSomething(null, a->{
                edit(after,before);
            });
        }else {
            actionStack.push(beanAction);
            undoStack.pop();
        }
    }

    @Override
    public boolean isUndo() {
        return !actionStack.empty();
    }

    @Override
    @SuppressWarnings({"Duplicates", "unchecked"})
    public void redo() {
        if (!isRedo()) {
            return;
        }
        BeanAction<T> beanAction = undoStack.pop();
        actionStack.push(beanAction);
        if (beanAction.getAction() == BeanAction.ACTION_ADD){
            editSomething(beanAction.getBefore(), b-> {
                return data.addAll(b);
            });
        }else if (beanAction.getAction() == BeanAction.ACTION_DEL){
            editSomething(beanAction.getBefore(), b->{
                return data.removeAll(b);
            });
        }else if (beanAction.getAction() == BeanAction.ACTION_EDIT){
            final T before = (T) beanAction.getBefore().toArray()[0];
            final T after = (T) beanAction.getAfter().toArray()[0];
            editSomething(null, a->{
                edit(before,after);
            });
        }else {
            undoStack.push(beanAction);
            actionStack.pop();
        }
    }

    @Override
    public boolean isRedo() {
        return !undoStack.empty();
    }

    public class BeanAction<t>{

        private Collection<t> before;

        private Collection<t> after;

        private final int action;

        static final int ACTION_ADD = 1;
        static final int ACTION_DEL = 2;
        static final int ACTION_EDIT = 3;

        BeanAction(int action) {
            this.action = action;
        }

        Collection<t> getBefore() {
            return before;
        }

        void setBefore(Collection<t> before) {
            this.before = before;
        }

        void setBefore(t before){
            this.before = new ArrayList<>(1);
            this.before.add(before);
        }

        Collection<t> getAfter() {
            return after;
        }

        void setAfter(Collection<t> after) {
            this.after = after;
        }

        void setAfter(t after){
            this.after = new ArrayList<>(1);
            this.after.add(after);
        }

        int getAction() {
            return action;
        }

    }

}
