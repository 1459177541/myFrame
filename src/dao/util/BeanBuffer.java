package dao.util;

import dao.db.annotation.DB_table;
import dao.db.sql.Select;
import dao.sf.annotation.SystemFile;
import dao.systemFile.SFUtil;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.stream.Stream;

public class BeanBuffer<T> {

    private ArrayList<T> data;

    private BeanBufferState state = BeanBufferState.INIT;

    private Class<T> clazz;

    public static final int METHOD_DB = 0;
    public static final int METHOD_SF = 1;

    public BeanBuffer(Class<T> clazz){
        this.clazz = clazz;
    }

    public BeanBufferState getState(){
        return state;
    }

    public synchronized void load(){
        if(clazz.isAnnotationPresent(DB_table.class)){
            load(METHOD_DB);
        }else if (clazz.isAnnotationPresent(SystemFile.class)){
            load(METHOD_SF);
        }
    }

    public synchronized void load(int method){
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
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
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
        if(BeanBufferState.LOADING == state){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    public synchronized void save(){
        if(clazz.isAnnotationPresent(DB_table.class)){
            load(METHOD_DB);
        }else if (clazz.isAnnotationPresent(SystemFile.class)){
            load(METHOD_SF);
        }
    }

    public synchronized void save(int method){
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

    public Stream<T> stream(){
        return data.stream();
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

}
