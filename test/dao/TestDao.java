package dao;

import dao.util.BeanBuffer;
import dao.util.FileStoreDao;
import factory.BeanBufferFactory;
import org.junit.jupiter.api.Test;

public class TestDao {

    @SuppressWarnings("unchecked")
    @Test
    public void testFactory(){
        BeanBufferFactory factory = new BeanBufferFactory().setDao(new FileStoreDao());
        BeanBuffer<Model> beanBuffer = (BeanBuffer<Model>) factory.get(Model.class);

        try{
            Thread.sleep(200);
        }catch (InterruptedException e){
        	e.printStackTrace();
        }

        beanBuffer.add(new Model("Test1",1));
        beanBuffer.add(new Model("Test2",2));
        beanBuffer.add(new Model("Test3",3));
        beanBuffer.add(new Model("Test4",4));

        beanBuffer.forEach(System.out::println);
    }

}
