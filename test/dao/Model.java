package dao;

import dao.sf.annotation.SystemFile;

import java.io.Serializable;

@SystemFile(fileName = "../test")
public class Model implements Serializable {

    public static final long serialVersionUID = 1L;

    private String name;

    private int id;

    public Model(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public Model() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Model{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
