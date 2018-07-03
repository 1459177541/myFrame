package dao.util;

import dao.db.annotation.DB_table;
import dao.db.sql.Select;
import dao.sf.annotation.SystemFile;
import dao.systemFile.SFUtil;
import util.asynchronized.AsyncExecuteManage;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public class BeanBuffer<T> implements Collection<T> {

    private ArrayList<T> data;

    private BeanBufferState state = BeanBufferState.INIT;

    private Class<T> clazz;

    private ReadWriteLock lock;

    public static final int METHOD_DB = 0;
    public static final int METHOD_SF = 1;

    public BeanBuffer(Class<T> clazz){
        lock = new ReentrantReadWriteLock();
        this.clazz = clazz;
    }

    public BeanBufferState getState(){
        return state;
    }

    public void load(){
        lock.writeLock().lock();
        try{
            if(clazz.isAnnotationPresent(DB_table.class)){
                doLoad(METHOD_DB);
            }else if (clazz.isAnnotationPresent(SystemFile.class)){
                doLoad(METHOD_SF);
            }
        }finally {
            lock.writeLock().unlock();
        }
    }

    public void load(int method){
        lock.writeLock().lock();
        try{
            doLoad(method);
        }finally {
            lock.writeLock().unlock();
        }
    }

    private void doLoad(int method){
        state = BeanBufferState.LOADING;
        if (METHOD_DB == method){
            loadByDB();
        }else if (METHOD_SF == method){
            loadBySF();
        }
        state = BeanBufferState.COMPLETE;
        notifyAll();
    }

    private void loadByDB(){
        if(!clazz.isAnnotationPresent(DB_table.class)){
            throw new RuntimeException("无法从数据库读取");
        }
        try {
            data = ((Select<T>)new Select<>().setObj(clazz.newInstance())).getResult();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void loadBySF(){
        if(!clazz.isAnnotationPresent(SystemFile.class)){
            throw new RuntimeException("无法从文件读取");
        }
        try {
            data = SFUtil.read(clazz);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 得到数据
     * @return 包含数据的列表
     */
    public ArrayList<T> get(){
        return readSomething(null, a->new ArrayList<>(data));
    }

    public void save(){
        readSomething(null, a->{
            if(clazz.isAnnotationPresent(DB_table.class)){
                doSave(METHOD_DB);
            }else if (clazz.isAnnotationPresent(SystemFile.class)){
                doSave(METHOD_SF);
            }
            return null;
        });
    }

    public void save(int method){
        readSomething(method, saveMethod->{
            doSave(saveMethod);
            return null;
        });
    }

    private void doSave(int method){
        state = BeanBufferState.SAVE;
        if (METHOD_DB == method){
            saveByDB();
        }else if (METHOD_SF == method){
            saveBySF();
        }
        state = BeanBufferState.COMPLETE;
        notifyAll();
    }

    private void saveBySF() {
        if(!clazz.isAnnotationPresent(SystemFile.class)){
            throw new RuntimeException("无法从文件读取");
        }
        try {
            SFUtil.write(data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void saveByDB() {

    }

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
        return readSomething(a, t-> data.toArray(a));
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
        editSomething(data, (Consumer<ArrayList<T>>) ArrayList::clear);
    }

    @Override
    public Stream<T> stream(){
        return readSomething(null, a-> data.stream());
    }

    /**
     * 等待
     * @throws InterruptedException
     */
    public synchronized void await() throws InterruptedException {
        while (!isCompleted()) {
            wait();
        }
    }

    public boolean isCompleted() {
        return BeanBufferState.COMPLETE == state;
    }

    /**
     * 遍历列表，将每个元素新建线程执行更新方法
     * @param list 待更新的缓存列表
     */
    public void synchronization(List<BeanBuffer> list){
        state = BeanBufferState.SYNCHRONIZATION;
        list.forEach(e -> AsyncExecuteManage.start(()-> readSomething(e, beanBuffer -> {
            beanBuffer.update(data);
            return null;
        })));
        state = BeanBufferState.COMPLETE;
    }

    private <A, R> R editSomething(A arg, Function<A, R> function){
        if (BeanBufferState.INIT == state){
            load();
        }
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

    private <A> void editSomething(A arg, Consumer<A> consumer){
        if (BeanBufferState.INIT == state){
            load();
        }
        state = BeanBufferState.EDITING;
        lock.writeLock().lock();
        try {
            consumer.accept(arg);
        }finally {
            lock.writeLock().unlock();
            state = BeanBufferState.COMPLETE;
        }
    }

    private <A,R> R readSomething(A arg, Function<A, R> function){
        if (BeanBufferState.INIT == state){
            load();
        }
        while(isCompleted()){
            try {
                await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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
