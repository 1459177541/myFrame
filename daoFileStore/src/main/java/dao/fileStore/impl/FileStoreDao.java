package dao.fileStore.impl;

import asynchronous.executor.AsyncExecuteManage;
import dao.fileStore.annotation.SystemFile;
import dao.fileStore.systemFile.SFUtil;
import dao.service.Dao;
import dao.beanBuffer.AbstractBeanBuffer;
import dao.beanBuffer.BeanBuffer;
import dao.beanBuffer.BeanBufferState;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Properties;

import static asynchronous.executor.AsyncLevel.SYSTEM;

public class FileStoreDao implements Dao {

    private Properties properties;

    public FileStoreDao() {
    }

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

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    @Override
    public void saveProperties(String propertiesFileName) throws IOException {
        properties.store(new FileOutputStream(propertiesFileName),"fileStore config");
    }


    private class FileStoreBeanBuffer<T> extends AbstractBeanBuffer<T> {

        FileStoreBeanBuffer(Class<T> clazz) {
            super(clazz);
            AsyncExecuteManage.start(SYSTEM, this::load);
        }

        @Override
        protected void load(){
            try {
                if (null != properties) {
                    data = SFUtil.read(properties.getProperty(clazz.getName()));
                }else {
                    data = SFUtil.read(clazz);
                }
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
                    if (null != properties) {
                        SFUtil.write(data, properties.getProperty(clazz.getName()));
                    } else {
                        SFUtil.write(data);
                    }
                } catch (FileNotFoundException e) {
                    throw new NoSuchElementException(e.getMessage());
                }
                actionStack.removeAllElements();
                undoStack.removeAllElements();
                return null;
            });
        }
    }
}
