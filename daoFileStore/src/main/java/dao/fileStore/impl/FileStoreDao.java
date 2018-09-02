package dao.fileStore.impl;

import asynchronous.executor.AsyncExecuteManage;
import dao.fileStore.annotation.SystemFile;
import dao.fileStore.systemFile.SFUtil;
import dao.service.Dao;
import dao.beanBuffer.AbstractBeanBuffer;
import dao.beanBuffer.BeanBuffer;
import dao.beanBuffer.BeanBufferState;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.stream.Collectors;

import static asynchronous.executor.AsyncLevel.SYSTEM;

public class FileStoreDao implements Dao{

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
    public boolean loadProperties(Properties properties) {
        this.properties = properties;
        return true;
    }

    @Override
    public boolean saveProperties(File propertiesFile) {
        try {
            properties.store(new FileOutputStream(propertiesFile), "file store config");
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private class FileStoreBeanBuffer<T> extends AbstractBeanBuffer<T> {

        private LoadBeanFactory beanFactory = LoadBeanFactory.INSTANCE;

        FileStoreBeanBuffer(Class<T> clazz) {
            super(clazz);
            AsyncExecuteManage.start(SYSTEM, this::load);
        }

        @Override
        protected void load(){
            try {
                List data;
                if (null != properties) {
                    data = SFUtil.read(properties.getProperty(clazz.getName()));
                }else {
                    data = SFUtil.read(clazz);
                }
                //noinspection unchecked
                this.data = (List<T>) data.stream().map(d->beanFactory.handled(d)).collect(Collectors.toList());
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
                        SFUtil.write(getRawData(), properties.getProperty(clazz.getName()));
                    } else {
                        SFUtil.write(getRawData());
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
