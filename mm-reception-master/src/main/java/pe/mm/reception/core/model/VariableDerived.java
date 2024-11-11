package pe.mm.reception.core.model;

import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;

import java.io.Serializable;

public class VariableDerived extends Variable implements Serializable {
    @NotNull(profiles = {"update","insert"})
    @NotEmpty(profiles = {"update","insert"})
    private String formula;
    private int idFile;
    private TableName table;

    public VariableDerived(){
        super(); setType(Variable.TYPE_DERIVED);
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }


    public int getIdFile() {
        return idFile;
    }

    public void setIdFile(int idFile) {
        this.idFile = idFile;
    }

    public TableName getTable() {
        return table;
    }

    public void setTable(TableName table) {
        this.table = table;
    }
}
