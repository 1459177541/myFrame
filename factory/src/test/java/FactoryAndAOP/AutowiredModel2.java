package FactoryAndAOP;

import frame.Autowired;
import frame.Bean;

@Bean(name = "autoModel2")
public class AutowiredModel2 {

    @Autowired(defaultInt = 2)
    private int id;
    @Autowired(defaultString = "AutowiredModel",order = 1)
    private String name;

    public AutowiredModel2(@Autowired(defaultInt = 2) int i, @Autowired(defaultString = "AutowiredModel") String s){
        System.out.println(s+i);
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

    @Override
    public String toString() {
        return "AutowiredModel2{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
