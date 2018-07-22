package FactoryAndAOP;

import build.FactoryBuilder;
import factory.BeanDefinition;
import factory.ConfigFactory;
import frame.config.FactoryConfig;
import org.junit.jupiter.api.Test;

public class OtherClassTest {

    @Test
    public void test(){
        ConfigFactory factory = new FactoryBuilder(new FactoryConfig() {
            @Override
            protected void initConfig() {
                try {
                    add("outerClass"
                            ,"../myFrame/target/test-classes"
                            ,"FactoryAndAOP.AutowiredModel3");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).build();

        AutowiredModel3Interface model3 = (AutowiredModel3Interface) factory.get("outerClass");
        System.err.println("main complete");
        model3.testAsync();
        System.err.println("main execute");
        try{
            Thread.sleep(1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

    }


}
