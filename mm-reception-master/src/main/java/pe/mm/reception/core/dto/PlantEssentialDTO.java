package pe.mm.reception.core.dto;

import pe.mm.reception.core.model.Plant;

import java.util.Date;

public class PlantEssentialDTO {
    private int id;
    private String name;
    private String timeZone;
    private String information;
    private Date create;
    private Date update;
    public PlantEssentialDTO() {
    }

    public PlantEssentialDTO(Plant plant){
        id = plant.getId();
        name = plant.getName();
        timeZone = plant.getTimeZone();
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

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
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
