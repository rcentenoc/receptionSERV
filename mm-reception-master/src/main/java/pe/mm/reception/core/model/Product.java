package pe.mm.reception.core.model;

import java.util.Date;
import java.util.List;

public class Product {
    private int id;
    private Line line;
    private String code;
    private String description;
    private Double ratedSpeed;
    private Double measurePerUnit;
    private Date create;
    private Date update;
    private List<Variable> characteristics;
    private List<String> values;

    public Product() {
    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getRatedSpeed() {
        return ratedSpeed;
    }

    public void setRatedSpeed(Double ratedSpeed) {
        this.ratedSpeed = ratedSpeed;
    }

    public Double getMeasurePerUnit() {
        return measurePerUnit;
    }

    public void setMeasurePerUnit(Double measurePerUnit) {
        this.measurePerUnit = measurePerUnit;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Variable> getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(List<Variable> characteristics) {
        this.characteristics = characteristics;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }
}
