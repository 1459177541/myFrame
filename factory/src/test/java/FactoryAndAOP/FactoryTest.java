package FactoryAndAOP;


import config.FactoryConfig;
import config.FactoryConfigByAnnotation;
import factory.ConfigFactory;
import factory.build.FactoryBuilder;
import org.junit.jupiter.api.Test;

public class FactoryTest {

    @Test
    public void test(){
        FactoryBuilder fb = new FactoryBuilder(new FactoryConfig() {
            @Override
            public void initConfig() {
                add("AOPImp", AOPImp.class);
                add("aopTest", AopTest.class);
            }
        });
        ConfigFactory f = fb.build();
        print(f);
    }
    @Test
    public void testAnnotation(){
        FactoryBuilder fb = new FactoryBuilder(new FactoryConfigByAnnotation() {
            @Override
            public void initConfig() {
                add(AOPImp.class);
                add(AopTest.class);
            }
        });
        ConfigFactory f = fb.build();
        print(f);
    }

    @Test
    public void testRoot(){
        new FactoryConfigByAnnotation() {
            @Override
            protected void initConfig() {
                add(AOPImp.class);
                add(AopTest.class);
            }
        }.setRoot();
        ConfigFactory f = new FactoryBuilder(new FactoryConfigByAnnotation() {
            @Override
            protected void initConfig() {
                add(AopTest2.class);    //覆盖AopTest
            }
        }).build();
        print(f);

    }

    private void print(ConfigFactory f){
        Object t = f.get("AOPImp");
        await();
        ((AOP01) t).print();
        await();
        ((AOP02) t).print("Hello World");
        await();
        ((AOP02) t).print("GOODBYE", " ", "WORLD");
        await();
        ((AOP02) t).print();
        await();
        ((AOP01) t).print(5, 7);
        await();
        ((AOP02) t).print(((AOP01) t).add(5, 7)+"");
        await();
    }

    private void await() {
        System.out.println(
                "----------------------------------------------------------------------------------------------------------------------------");
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
