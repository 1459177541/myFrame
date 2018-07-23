package FactoryAndAOP;


import frame.Autowired;
import frame.Bean;

@Bean(name = "autoModel1")
public class AutowiredModel {

    private int id;

    private String name;

    @Autowired(order = 2)
    public AutowiredModel() {

    }

    @Autowired(order = 1)
    public AutowiredModel(@Autowired(defaultInt = 1) int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Autowired(order = 3)
    public AutowiredModel(@Autowired(name = "autoModel2") AutowiredModel2 autowiredModel2){
        System.out.println("auto");
        System.out.println(autowiredModel2);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Autowired
    public void autoMethod(@Autowired(defaultString = "autoMethod0") String text){
        System.err.println(text);
    }

    @Autowired(order = 1)
    public void autoMethod1(@Autowired(defaultString = "autoMethod1") String text){
        System.err.println(text);
    }

    @Override
    public String toString() {
        return "AutowiredModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
