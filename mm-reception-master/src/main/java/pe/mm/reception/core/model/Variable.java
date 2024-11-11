package pe.mm.reception.core.model;

import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;

import java.util.Date;

public class Variable {
    public static String TYPE_CONTINUING = "continuing";
    public static String TYPE_DERIVED= "derived";
    public static String TYPE_FIXED = "fixed";

    @NotNull(profiles = {"update", "delete"})
    protected int id;
    @NotNull(profiles = {"update", "insert"})
    @NotEmpty(profiles = {"update", "insert"})
    protected String name;
    @Length(max = 10, message = "Como máximo 10 letras para la unidad",profiles = {"insert","update"})
    @NotNull(profiles = {"update", "insert"})
    protected String unit;
    @NotNull(profiles = {"update", "insert"})
    protected int status;
    @NotNull(profiles = {"update", "insert"})
    @NotEmpty(profiles = {"update", "insert"})
    protected String shortName;
    protected Date create;
    protected Date update;
    private String type;
    protected Integer related;
    @NotNull(target="id",profiles = {"update", "insert"})
    protected Device device;

    protected int genericType;
    protected int relatedIdVariable;


    public Variable() {

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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
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

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getGenericType() {
        return genericType;
    }

    public void setGenericType(int genericType) {
        this.genericType = genericType;
    }

    public Integer getRelated() {
        return related;
    }

    public void setRelated(Integer related) {
        this.related = related;
    }

    public int getRelatedIdVariable() {
        return relatedIdVariable;
    }

    public void setRelatedIdVariable(int relatedIdVariable) {
        this.relatedIdVariable = relatedIdVariable;
    }
}
