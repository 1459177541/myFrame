package dao;


import dao.beanBuffer.BeanBuffer;
import factory.build.BeanBufferFactory;
import factory.build.DaoBuild;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestDao {

    private BeanBuffer<Model> beanBuffer;

    //TODO 加载不到服务
    @SuppressWarnings("unchecked")
    @Test
    @BeforeEach
    public void testFactory(){
        BeanBufferFactory factory = new BeanBufferFactory();
        beanBuffer = (BeanBuffer<Model>) factory.get(Model.class);
    }

    @Test
    public void testMethod(){
        if (beanBuffer.size()==0) {
            add();
        }
        print("init");
        Model m = new Model("Test5",5);

        beanBuffer.add(m);

        print("add");

        beanBuffer.undo();
        beanBuffer.undo();

        print("undo");

        beanBuffer.redo();

        print("redo");

        beanBuffer.add(m);
        beanBuffer.remove(m);

        print("remove");

        beanBuffer.undo();

        print("undo remove");

        beanBuffer.clear();

        print("clear");

        beanBuffer.undo();

        print("undo clear");
    }

    @Test
    public void testSave(){
        add();
        DaoBuild.getDao(Model.class).save(beanBuffer);
        System.out.println("完成");
    }

    @Test
    public void testLoad(){
        print("load");
    }

    private void add(){
        beanBuffer.add(new Model("Test1",1));
        beanBuffer.add(new Model("Test2",2));
        beanBuffer.add(new Model("Test3",3));
        beanBuffer.add(new Model("Test4",4));
    }

    private void print(String text){
        System.out.println("\n"+text);
        beanBuffer.forEach(System.out::println);
        System.out.println("/"+text);
    }

}
