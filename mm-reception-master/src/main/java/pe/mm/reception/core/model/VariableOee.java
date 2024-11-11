package pe.mm.reception.core.model;

import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;

import java.util.Date;

public class VariableOee {
    public static String TYPE_CONTINUING = "continuing";
    public static String TYPE_DERIVED= "derived";
    public static String TYPE_FIXED = "fixed";

    @NotNull(profiles = {"update", "delete"})
    protected int id;
    @NotNull(profiles = {"update", "insert"})
    @NotEmpty(profiles = {"update", "insert"})
    protected String name;
    @NotNull(profiles = {"update", "insert"})
    @NotEmpty(profiles = {"update", "insert"})
    protected String unit;
    @NotNull(profiles = {"update", "insert"})
    protected int status;
    @NotNull(profiles = {"update", "insert"})
    @NotEmpty(profiles = {"update", "insert"})
    protected String shortName;
    protected Date create;
    protected Date update;



    protected int standar;
    private String type;



    @NotNull(target="id",profiles = {"update", "insert"})
    protected GraphicVariable graphicVariable;


    public VariableOee() {

    }
    public int getStandar() {
        return standar;
    }

    public void setStandar(int standar) {
        this.standar = standar;
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



    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public GraphicVariable getGraphicVariable() {
        return graphicVariable;
    }

    public void setGraphicVariable(GraphicVariable graphicVariable) {
        this.graphicVariable = graphicVariable;
    }
}
