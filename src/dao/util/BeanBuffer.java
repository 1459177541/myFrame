package dao.util;

import dao.db.annotation.DB_table;
import dao.db.sql.Select;
import dao.sf.annotation.SystemFile;
import dao.systemFile.SFUtil;
import util.asynchronized.AsyncStaticExecute;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Stream;

public class BeanBuffer<T> {

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

    public ArrayList<T> get(){
        if (BeanBufferState.INIT == state){
            load();
        }
        if(BeanBufferState.LOADING == state || BeanBufferState.EDITING == state){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<>(data);
    }

    public void save(){
        lock.readLock().lock();
        try {
            if(clazz.isAnnotationPresent(DB_table.class)){
                doSave(METHOD_DB);
            }else if (clazz.isAnnotationPresent(SystemFile.class)){
                doSave(METHOD_SF);
            }
        }finally {
            lock.readLock().unlock();
        }
    }

    public void save(int method){
        lock.readLock().lock();
        try{
            doSave(method);
        }finally {
            lock.readLock().unlock();
        }
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
        lock.writeLock().lock();
        try {
            state = BeanBufferState.EDITING;
            data = new ArrayList<>(list);
            state = BeanBufferState.COMPLETE;
            notifyAll();
        }finally {
            lock.writeLock().unlock();
        }
    }

    public Stream<T> stream(){
        return new ArrayList<>(data).stream();
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

    public void synchronization(List<BeanBuffer> list){
        state = BeanBufferState.SYNCHRONIZATION;
        ArrayList<T> tArrayList = new ArrayList<>(data);
        list.forEach(e -> AsyncStaticExecute.start(() -> {
            lock.readLock().lock();
            try {
                e.update(tArrayList);
            }finally {
                lock.readLock().unlock();
            }
        }));
        state = BeanBufferState.COMPLETE;
    }

}
