package dao.fileStore.imp;

import asynchronous.executor.AsyncExecuteManage;
import dao.fileStore.systemFile.SFUtil;
import dao.frame.Dao;
import dao.util.AbstractBeanBuffer;
import dao.util.BeanBuffer;
import dao.util.BeanBufferState;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import static asynchronous.executor.AsyncLevel.SYSTEM;

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
    @SuppressWarnings({"unchecked", "ClassEscapesDefinedScope"})
    public <T> FileStoreBeanBuffer<T> load(Class<T> clazz) {
        return new FileStoreBeanBuffer(clazz);
    }


    private class FileStoreBeanBuffer<T> extends AbstractBeanBuffer<T> {

        FileStoreBeanBuffer(Class<T> clazz) {
            super(clazz);
            AsyncExecuteManage.start(SYSTEM, this::load);
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
