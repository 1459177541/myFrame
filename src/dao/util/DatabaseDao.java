package dao.util;

import dao.db.sql.*;
import dao.db.util.GetConn;
import dao.frame.Dao;
import util.asynchronized.AsyncExecuteManage;

public class DatabaseDao implements Dao {

    private final GetConn conn;

    public DatabaseDao(){
        conn = GetConn.getDefault();
    }

    public DatabaseDao(String name, String password) {
        conn = new GetConn(name, password);
    }

    public DatabaseDao(String name, String password, String driver, String url){
        conn = new GetConn(name, password, driver, url);
    }

    @Override
    public void save(BeanBuffer beanBuffer) {
        if (beanBuffer instanceof DBBeanBuffer) {
            AsyncExecuteManage.start(()->((DBBeanBuffer) beanBuffer).save(conn));
        }else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> DBBeanBuffer<T> load(Class<T> clazz) {
        return new DBBeanBuffer<>(clazz);
    }

    private class DBBeanBuffer<T> extends AbstractBeanBuffer<T> {

        DBBeanBuffer(Class<T> clazz) {
            super(clazz);
            AsyncExecuteManage.start(this::load);
        }

        private void load(){
            Select<T> select = new Select<>();
            select.setClazz(clazz);
            select.setConnection(conn.getConn());
            data = select.getResult();
            state = BeanBufferState.COMPLETE;
            stopWait();
        }

        @SuppressWarnings("unchecked")
        void save(GetConn conn){
            if (!isUndo()){
                return;
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
        }

    }

}
