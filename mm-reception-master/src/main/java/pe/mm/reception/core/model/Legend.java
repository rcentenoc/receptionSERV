package pe.mm.reception.core.model;


import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;

import java.util.Date;

/**
 * Created by César Calle on 14/09/2016.
 */
public class Legend {

    @NotNull(profiles = {"update", "delete"})
    private int id;
    @NotNull(profiles = {"update", "insert"})
    @NotEmpty(profiles = {"update", "insert"})
    private String value;
    @NotNull(profiles = {"update", "insert"})
    @NotEmpty(profiles = {"update", "insert"})
    private String name;
    @NotNull(profiles = {"update", "insert"})
    @NotEmpty(profiles = {"update", "insert"})
    private String description;
    private Date create;
    private Date update;
    @NotNull(profiles = {"update", "insert"})
    private VariableContinuing variableContinuing;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description= description;
    }

    public VariableContinuing getVariableContinuing() {
        return variableContinuing;
    }

    public void setVariableContinuing(VariableContinuing variableContinuing) {
        this.variableContinuing = variableContinuing;
    }
    public Date getUpdate() {
        return update;
    }

    public void setUpdate(Date update) {
        this.update = update;
    }

    public Date getCreate() {
        return create;
    }

    public void setCreate(Date create) {
        this.create = create;
    }

}
