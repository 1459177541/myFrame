package FactoryAndAOP;

import build.FactoryBuilder;
import factory.ConfigFactory;
import frame.config.FactoryConfig;
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

    public void await() {
        System.out.println(
                "----------------------------------------------------------------------------------------------------------------------------");
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
