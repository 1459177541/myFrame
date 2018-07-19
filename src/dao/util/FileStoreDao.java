package dao.util;

import dao.frame.Dao;
import dao.systemFile.SFUtil;
import util.asynchronized.AsyncExecuteManage;

import java.io.FileNotFoundException;
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
            AsyncExecuteManage.start(this::load);
        }

        private void load(){
            editSomething(null,a->{
                try {
                    data = SFUtil.read(clazz);
                } catch (FileNotFoundException e) {
                    throw new NoSuchElementException(e.getMessage());
                }
            });
        }

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
