package dao.fileStore.imp;

import asynchronous.executor.AsyncExecuteManage;
import dao.fileStore.annotation.SystemFile;
import dao.fileStore.systemFile.SFUtil;
import dao.service.Dao;
import dao.beanBuffer.AbstractBeanBuffer;
import dao.beanBuffer.BeanBuffer;
import dao.beanBuffer.BeanBufferState;

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

    @Override
    public <T> boolean isCanLoad(Class<T> clazz) {
        return clazz.isAnnotationPresent(SystemFile.class);
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
                state = BeanBufferState.COMPLETE;
                stopWait();
            }
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
