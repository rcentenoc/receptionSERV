package pe.mm.reception.core.model;

import java.io.Serializable;

public class VariableFixedBasic implements Serializable {

    private int id;
    private Double value;

    public VariableFixedBasic(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
