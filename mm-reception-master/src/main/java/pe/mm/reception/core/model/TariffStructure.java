package pe.mm.reception.core.model;

import net.sf.oval.constraint.Min;
import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;

import java.util.Date;
import java.util.List;

public class TariffStructure {
    @NotNull(profiles = {"update,delete"})
    private  int id;
    @NotNull(profiles = {"insert","update"})
    @NotEmpty(profiles = {"insert","update"})
    private String name;
    @NotNull(profiles = {"insert","update"})
    @NotEmpty(profiles = {"insert","update"})
    private String country;
    private Date create;
    private Date update;
    @Min(value = 1,profiles = {"insert","update"})
    private int periods;
    private List<Integer> idsPlant;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    public int getPeriods() {
        return periods;
    }

    public void setPeriods(int periods) {
        this.periods = periods;
    }

    public List<Integer> getIdsPlant() {
        return idsPlant;
    }

    public void setIdsPlant(List<Integer> idsPlant) {
        this.idsPlant = idsPlant;
    }
}
