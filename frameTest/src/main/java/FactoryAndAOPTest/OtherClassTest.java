package FactoryAndAOPTest;

import config.FactoryConfig;
import factory.BeanFactory;
import factory.build.FactoryBuilder;
import org.junit.jupiter.api.Test;

public class OtherClassTest {

    @Test
    public void test(){
        BeanFactory factory = new FactoryBuilder(new FactoryConfig() {
            @Override
            protected void initConfig() {
                try {
                    add("outerClass"
                            ,"../myFrame/target/test-classes"
                            ,"FactoryAndAOPTest.AutowiredModel3");
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
