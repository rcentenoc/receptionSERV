package pe.mm.reception.core.model;

import net.sf.oval.constraint.NotNull;

import java.io.Serializable;
import java.util.Date;

public class Rol implements Serializable {
    @NotNull(profiles = {"update","delete"})
    private int id;
    private String description;
    private Date create;
    private Date update;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreate() {
        return create;
    }

    public void setCreate(Date create) {
        this.create = create;
    }

    public Date getUpdate() {
        return update;
    }

    public void setUpdate(Date update) {
        this.update = update;
    }
}
