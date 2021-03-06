package dao.db.impl;

import asynchronous.executor.AsyncExecuteManage;
import dao.db.annotation.DB_table;
import dao.db.sql.*;
import dao.db.util.GetConn;
import dao.service.Dao;
import dao.beanBuffer.AbstractBeanBuffer;
import dao.beanBuffer.BeanBuffer;
import dao.beanBuffer.BeanBufferState;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

import static asynchronous.executor.AsyncLevel.SYSTEM;


public class DatabaseDao implements Dao {

    private GetConn conn;

    public DatabaseDao(){
        conn = GetConn.getDefault();
    }

    public DatabaseDao(String name, String password, String databaseName) {
        conn = new GetConn(name, password, databaseName);
    }

    public DatabaseDao(String name, String password, String driver, String url){
        conn = new GetConn(name, password, driver, url);
    }

    @Override
    public void save(BeanBuffer beanBuffer) {
        if (beanBuffer instanceof DBBeanBuffer) {
            AsyncExecuteManage.start(SYSTEM,()->((DBBeanBuffer) beanBuffer).save(conn));
        }else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    @SuppressWarnings({"unchecked", "ClassEscapesDefinedScope"})
    public <T> DBBeanBuffer<T> load(Class<T> clazz) {
        return new DBBeanBuffer<>(clazz);
    }

    @Override
    public <T> boolean isCanLoad(Class<T> clazz) {
        return clazz.isAnnotationPresent(DB_table.class);
    }

    @Override
    public boolean loadProperties(Properties properties) {
        conn = new GetConn(properties);
        return true;
    }

    @Override
    public boolean saveProperties(File propertiesFile) {
        return conn.saveProperties(propertiesFile);
    }


    private class DBBeanBuffer<T> extends AbstractBeanBuffer<T> {

        private LoadBeanFactory beanFactory = LoadBeanFactory.INSTANCE;

        DBBeanBuffer(Class<T> clazz) {
            super(clazz);
            AsyncExecuteManage.start(SYSTEM,this::load);
        }

        @Override
        protected void load(){
            try {
                Select<T> select = new Select<>();
                select.setClazz(clazz);
                select.setConnection(conn.getConn());
                List data = Objects.requireNonNull(select.getResult());
                //noinspection unchecked
                this.data = (List<T>) data.stream().map(d->beanFactory.handled(d)).collect(Collectors.toList());
            }finally {
                Objects.requireNonNullElseGet(data,ArrayList::new);
                state = BeanBufferState.COMPLETE;
                stopWait();
            }
        }

        @Override
        protected void save() {
            save(Objects.requireNonNullElseGet(conn,GetConn::getDefault));
        }

        @SuppressWarnings("unchecked")
        void save(GetConn conn){
            if (!isUndo()){
                return;
            }
            if (!conn.hasTable(clazz.getAnnotation(DB_table.class).tableName())){
                CreateTable createTable = new CreateTable();
                createTable.setClazz(clazz);
                createTable.setConnection(conn.getConn());
                createTable.execute();
            }
            actionStack.forEach(e->{
                Result<T> database;
                if (e.getAction() == BeanAction.ACTION_ADD){
                    database = new Add<>();
                    database.setObj((T) e.getBefore().toArray()[0]);
                }else if (e.getAction() == BeanAction.ACTION_DEL){
                    database = new Del<>();
                    database.setObj((T) e.getBefore().toArray()[0]);
                }else if (e.getAction() == BeanAction.ACTION_EDIT){
                    database = new Edit<>();
                    database.setObj((T) e.getAfter().toArray()[0]);
                    ((Edit<T>) database).setOldObj((T) e.getBefore().toArray()[0]);
                }else {
                    return;
                }
                database.setClazz(clazz);
                database.setConnection(conn.getConn());
                database.execute();
            });
            actionStack.removeAllElements();
            undoStack.removeAllElements();
        }

    }

}
