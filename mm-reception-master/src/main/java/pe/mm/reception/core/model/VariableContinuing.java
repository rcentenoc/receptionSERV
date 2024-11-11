package pe.mm.reception.core.model;

import net.sf.oval.constraint.NotEqual;

import java.io.Serializable;

public class VariableContinuing extends Variable implements Serializable {
    private String column;


    @NotEqual(value = "0",message = "Debe seleccionar un archivo", profiles = {"update","insert"})
    private  int  idFile;

    public VariableContinuing() {
        setType(Variable.TYPE_CONTINUING);
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public int getIdFile() {
        return idFile;
    }

    public void setIdFile(int idFile) {
        this.idFile = idFile;
    }
}
