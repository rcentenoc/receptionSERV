package pe.mm.reception.core.model;

import pe.mm.reception.core.dto.DateOfMemory;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class MemoryForVariablesMixed implements DateOfMemory, Serializable {
    private int idTableName;
    /**
     * referencia a datos del padre 1
     */
    private String memory1;
    /**
     * referencia a datos del padre 2
     */
    private String memory2;
    private TableName tableName;
    /**
     * lista de variables mixtas derivadas de una tabla
     */
    private List<VariableDerived> deriveds;
    /**
     * fecha de los datos
     */
    private Date time;


    public TableName getTableName() {
        return tableName;
    }

    public int getIdTableName() {
        return idTableName;
    }

    public void setIdTableName(int idTableName) {
        this.idTableName = idTableName;
    }

    public void setTableName(TableName tableName) {
        this.tableName = tableName;
    }

    public List<VariableDerived> getDeriveds() {
        return deriveds;
    }

    public void setDeriveds(List<VariableDerived> deriveds) {
        this.deriveds = deriveds;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getMemory1() {
        return memory1;
    }

    public void setMemory1(String memory1) {
        this.memory1 = memory1;
    }

    public String getMemory2() {
        return memory2;
    }

    public void setMemory2(String memory2) {
        this.memory2 = memory2;
    }

    @Override
    public Date getDate() {
        return time;
    }
}