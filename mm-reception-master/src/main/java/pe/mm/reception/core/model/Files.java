package pe.mm.reception.core.model;

import java.io.Serializable;
import java.util.Date;

public class Files implements Serializable {
    private int id;
    private String name;
    private Date create;
    private Date update;
    private Plant plant;
    private String path;
    private String secondName;
    private int type;
    private int sendFrequency;
    public Files(){}

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

    public int getSendFrequency() {
        return sendFrequency;
    }

    public void setSendFrequency(int sendFrequency) {
        this.sendFrequency = sendFrequency;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
