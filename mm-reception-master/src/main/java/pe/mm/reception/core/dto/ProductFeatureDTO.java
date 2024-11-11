package pe.mm.reception.core.dto;

import java.util.Date;

public class ProductFeatureDTO {
    private int id;
    private int idData;
    private String name;
    private Double quantity;
    private Date time;

    public ProductFeatureDTO() {

    }


    public int getIdData() {
        return idData;
    }

    public void setIdData(int idData) {
        this.idData = idData;
    }
    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
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
}
