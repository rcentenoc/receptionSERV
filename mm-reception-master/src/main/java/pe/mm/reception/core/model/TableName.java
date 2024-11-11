package pe.mm.reception.core.model;

import java.io.Serializable;

public class TableName implements Serializable {
    private String name;
    private String parent1;
    private String parent2;
    private String index;
    private int id;

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

    public String getParent1() {
        return parent1;
    }

    public void setParent1(String parent1) {
        this.parent1 = parent1;
    }

    public String getParent2() {
        return parent2;
    }

    public void setParent2(String parent2) {
        this.parent2 = parent2;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }
}
