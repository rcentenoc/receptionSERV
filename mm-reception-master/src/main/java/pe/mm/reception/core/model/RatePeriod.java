package pe.mm.reception.core.model;

import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;

import java.util.Date;

public class RatePeriod {
    @NotNull(profiles = {"update", "delete"})
    private int id;
    @NotNull(profiles = {"update","insert"})
    @NotEmpty(profiles = {"update","insert"})
    private String description;
    private int coin;
    private double cost_kwh;
    private Date create;
    private Date update;
    @NotNull(profiles = {"update","insert"})
    private Plant plant;
    @NotNull(profiles = {"update","insert"})
    private TariffStructure tariffStructure;

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

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public double getCost_kwh() {
        return cost_kwh;
    }

    public void setCost_kwh(double cost_kwh) {
        this.cost_kwh = cost_kwh;
    }

    public Date getCreate() {
        return create;
    }

    public void setCreate(Date created) {
        this.create = created;
    }

    public Date getUpdate() {
        return update;
    }

    public void setUpdate(Date updated) {
        this.update = updated;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    public TariffStructure getTariffStructure() {
        return tariffStructure;
    }

    public void setTariffStructure(TariffStructure tariffStructure) {
        this.tariffStructure = tariffStructure;
    }
}
