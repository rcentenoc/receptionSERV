package pe.mm.reception.core.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Line implements Serializable {
    private int id;
    private String name;
    private String information;
    private Date create;
    private Date update;
    private Plant plant;
    private String lineCode;
    private int statusTables;
    private int statusProduct;
    private List<Location> locations;
    private int batch;

    /**
     * {@value 0} Linea de tipo por defecto
     * {@value 1} Linea de tipo producción
     */
    private int type;

    public Line() {
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

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    public String getLineCode() {
        return lineCode;
    }

    public void setLineCode(String lineCode) {
        this.lineCode = lineCode;
    }

    public int getStatusTables() {
        return statusTables;
    }

    public void setStatusTables(int statusTables) {
        this.statusTables = statusTables;
    }

    public int getStatusProduct() {
        return statusProduct;
    }

    public void setStatusProduct(int statusProduct) {
        this.statusProduct = statusProduct;
    }

    public int getBatch() {
        return batch;
    }

    public void setBatch(int batch) {
        this.batch = batch;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
