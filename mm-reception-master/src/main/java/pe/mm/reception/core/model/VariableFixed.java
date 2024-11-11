package pe.mm.reception.core.model;

import javax.validation.constraints.NotNull;

public class VariableFixed extends Variable {
    @NotNull
    public Double value;

    public VariableFixed() {
        setType(Variable.TYPE_FIXED);
    }

    public void setValue(Double value){
        this.value = value;
    }

    public Double getValue() {
        return value;
    }
}
