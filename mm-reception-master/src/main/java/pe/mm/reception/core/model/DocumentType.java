package pe.mm.reception.core.model;

import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;

import java.io.Serializable;
import java.util.Date;

public class DocumentType implements Serializable {
    @NotNull(profiles = {"update","delete"})
    private int id;

    private Date create;
    private Date update;
    @NotNull(profiles = {"update","insert"})
    @NotEmpty(profiles = {"update","insert"})
    private String description;
    @NotNull(profiles = {"update","insert"})
    @NotEmpty(profiles = {"update","insert"})
    private String shortName;

    private String uniquecode;

    private int usermod;

    public DocumentType() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getUniquecode() {
        return uniquecode;
    }

    public void setUniquecode(String uniquecode) {
        this.uniquecode = uniquecode;
    }

    public void setUsermod(int usermod) {
        this.usermod = usermod;
    }
}
