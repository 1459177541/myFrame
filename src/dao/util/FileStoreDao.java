package dao.util;

import dao.frame.Dao;
import dao.systemFile.SFUtil;
import util.asynchronized.AsyncExecuteManage;
import util.asynchronized.AsyncLevel;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class FileStoreDao implements Dao {

    @Override
    public void save(BeanBuffer beanBuffer) {
        if (beanBuffer instanceof FileStoreBeanBuffer){
            ((FileStoreBeanBuffer)beanBuffer).save();
        }else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> FileStoreBeanBuffer load(Class<T> clazz) {
        return new FileStoreBeanBuffer(clazz);
    }


    private class FileStoreBeanBuffer<T> extends AbstractBeanBuffer<T>{

        public FileStoreBeanBuffer(Class<T> clazz) {
            super(clazz);
            AsyncExecuteManage.start(AsyncLevel.SYSTEM, this::load);
        }

        @Override
        protected void load(){
            try {
                data = SFUtil.read(clazz);
            } catch (FileNotFoundException e) {
//                throw new NoSuchElementException(e.getMessage());
                data = new ArrayList<>();
            }finally {
                stopWait();
            }
            state = BeanBufferState.COMPLETE;
        }

        @Override
        public void save(){
            readSomething(null, a->{
                try {
                    SFUtil.write(data);
                } catch (FileNotFoundException e) {
                    throw new NoSuchElementException(e.getMessage());
                }
                return null;
            });
        }
    }
}
