package pe.mm.reception.core.dto;

import net.sf.oval.constraint.NotNull;
import pe.mm.reception.core.model.Variable;
import pe.mm.reception.core.model.VariableDerived;

public class VariableDerivedDTO extends Variable {
    @NotNull(profiles = {"update","inserted"})
    private FormulaDerivedDTO formula;
    private int idFile;
    public VariableDerivedDTO() {
    }

    public VariableDerivedDTO(VariableDerived var) {
        id= var.getId();
        setType(var.getType());
        status = var.getStatus();
        unit = var.getUnit();
        shortName = var.getShortName();
        name = var.getName();
        device = var.getDevice();
        idFile=var.getIdFile();
        genericType = var.getGenericType();
        relatedIdVariable=var.getRelatedIdVariable();
        formula = new FormulaDerivedDTO(var.getFormula());
    }

    public FormulaDerivedDTO getFormula() {
        return formula;
    }

    public void setFormula(FormulaDerivedDTO formula) {
        this.formula = formula;
    }

    public int getIdFile() {
        return idFile;
    }

    public void setIdFile(int idFile) {
        this.idFile = idFile;
    }
}
