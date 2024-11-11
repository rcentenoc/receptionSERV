package pe.mm.reception.core.model;

import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;

import java.util.Date;

public class GraphicType {
    @NotNull(profiles = {"update","delete"})
    private int id;
    @NotNull (profiles = {"update","insert"})
    @NotEmpty(profiles = {"update","insert"})
    private String name;
    @NotNull (profiles = {"update","insert"})
    @NotEmpty (profiles = {"update","insert"})
    private String description;
    private Date create;
    private Date update;

    public GraphicType() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
