package pe.mm.reception.core.dto;

import pe.mm.reception.core.model.Variable;

public class VariableEssentialDTO {
    private int id;
    private String name;
    private String type;
    private String shortName;

    public VariableEssentialDTO() {
    }

    public VariableEssentialDTO(Variable variable) {
        id = variable.getId();
        name = variable.getName();
        type = variable.getType();
        shortName = variable.getShortName();
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}
