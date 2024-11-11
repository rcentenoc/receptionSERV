package pe.mm.reception.core.model;

import net.sf.oval.constraint.NotNull;

import java.util.Date;

public class Depletion {
    @NotNull(profiles = {"delete","update"})
    private int id;
    @NotNull(profiles = {"insert","update"})
    private double quantity;
    @NotNull(profiles = {"insert","update"})
    private String description;
    private Date create;
    private Date update;
    @NotNull(profiles = {"insert","update"})
    private Calendary calendary;
    private int calendaryId;

    public Depletion() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
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

    public Calendary getCalendary() {
        return calendary;
    }

    public void setCalendary(Calendary calendary) {
        this.calendary = calendary;
    }

    public int getCalendaryId() {
        return calendaryId;
    }

    public void setCalendaryId(int calendaryId) {
        this.calendaryId = calendaryId;
    }
}
