package FactoryAndAOP;

import build.FactoryBuilder;
import factory.ConfigFactory;
import frame.config.FactoryConfigByAnnotation;
import org.junit.jupiter.api.Test;

public class TestAutowired {

    @Test
    public void test(){
        FactoryBuilder builder = new FactoryBuilder(new FactoryConfigByAnnotation() {
            @Override
            protected void initConfig() {
                add(AutowiredModel.class);
                add(AutowiredModel2.class);
            }
        });
        ConfigFactory factory = builder.build();


        System.out.println(factory.get("autoModel1"));
    }

    @Test
    public void test2(){
        ConfigFactory factory = new FactoryBuilder(new FactoryConfigByAnnotation() {
            @Override
            protected void initConfig() {
                add(AutowiredModel3.class);
            }
        }).build();

        AutowiredModel3Interface model3 = (AutowiredModel3Interface) factory.get("autoModel3");
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
