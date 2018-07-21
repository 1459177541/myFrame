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

}
