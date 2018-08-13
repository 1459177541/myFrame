package FactoryAndAOPTest;


import frame.Autowired;
import frame.Bean;
import proxy.annotation.Async;

@Bean(name = "autoModel3")
@Async
public class AutowiredModel3 implements AutowiredModel3Interface{

    @Autowired(defaultInt = 3)
    int id;

    String name;

    public String getName() {
        return name;
    }

    @Autowired
    public void setName(@Autowired(defaultString = "text") String name) {
        this.name = name;
    }

    @Override
    @Async
    public void testAsync(){
        System.err.println(name+" start "+id);
        try{
            Thread.sleep(700);
        }catch (InterruptedException e){
        	e.printStackTrace();
        }
        System.err.println(name+" complete "+id);
    }

}
