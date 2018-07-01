package factory;

import dao.util.BeanBuffer;
import dao.util.BeanBufferState;
import util.asynchronized.AsyncExecuteManage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class BeanBufferFactory implements Factory<Class<?>, BeanBuffer<?>> {

    private HashMap<Class<?>,BeanBuffer<?>> data;

    private BeanBufferFactory parent;

    private ArrayList<BeanBufferFactory> child;

    public BeanBufferFactory() {
        data = new HashMap<>();
        child = new ArrayList<>();
    }

    public void setParent(BeanBufferFactory parent) {
        this.parent = parent;
    }

    public void addChild(BeanBufferFactory child){
        this.child.add(child);
    }

    public synchronized <T> void load(Class<T> clazz){
        if (data.containsKey(clazz)){
            return;
        }
        if (null!=parent && parent.contains(clazz)){
            data.put(clazz,data.get(clazz));
            return;
        }
        if (BeanBufferState.LOADING == data.get(clazz).getState()){
            return;
        }
        BeanBuffer<T> beanBuffer= new BeanBuffer<>(clazz);
        data.put(clazz,beanBuffer);
        AsyncExecuteManage.start(beanBuffer, BeanBuffer::load);
    }

    @Override
    public BeanBuffer<?> get(Class<?> key) {
        BeanBuffer beanBuffer;
        if (null!=parent){
            beanBuffer = parent.get(key);
        }else {
            beanBuffer = data.get(key);
        }
        if (!contains(key) || BeanBufferState.INIT == beanBuffer.getState()){
            load(key);
        }
        if (BeanBufferState.LOADING == beanBuffer.getState()){
            try {
                beanBuffer.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return beanBuffer;
    }

    public boolean contains(Class clazz){
        if (null!= parent && parent.contains(clazz)){
            return true;
        }
        return data.containsKey(clazz);
    }

    public void save(Class<?> clazz){
        if (!contains(clazz)){
            return;
        }
        if (BeanBufferState.COMPLETE == data.get(clazz).getState()){
            data.get(clazz).save();
        }
    }

    public <T> void update(Class<T> clazz, ArrayList<T> newList){
        if (!contains(clazz)){
            BeanBuffer<T> bf = new BeanBuffer<>(clazz);
            bf.update(newList);
            data.put(clazz, bf);
        }
        parent.update(clazz,newList);
    }

    public void synchronization(Class<?> clazz){
        List<BeanBuffer> beanBufferList = child.stream().map(e->e.get(clazz)).collect(Collectors.toList());
        get(clazz).synchronization(beanBufferList);
    }

}