package dao.util;

import dao.db.sql.Select;
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
            // TODO 未完成相关保存操作
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

        public DBBeanBuffer(Class<T> clazz) {
            super(clazz);
            AsyncExecuteManage.start(this::load);
        }

        private void load(){
            editSomething(null,a->{
                Select<T> select = new Select<>();
                select.setClazz(clazz);
                select.setConnection(conn.getConn());
                data = select.getResult();
            });
        }


    }

}
