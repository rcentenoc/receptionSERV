package pe.mm.reception.core.model;

import net.sf.oval.constraint.NotNull;

import java.util.Date;
import java.util.List;

public class GraphicVariable {
    private int id;
    @NotNull
    private Variable variable;
    @NotNull
    private Graphic graphic;
    private Date create;
    private int setting;
    private Variable related;
    private List<Value> values;

    public Date getCreate() {
        return create;
    }

    public void setCreate(Date create) {
        this.create = create;
    }

    public List<Value> getValues() {
        return values;
    }

    public void setValues(List<Value> values) {
        this.values = values;
    }

    public GraphicVariable() {
    }

    public Graphic getGraphic() {
        return graphic;
    }

    public void setGraphic(Graphic graphic) {
        this.graphic = graphic;
    }

    public Variable getVariable() {
        return variable;
    }

    public void setVariable(Variable variable) {
        this.variable = variable;
    }

    public int getSetting() {
        return setting;
    }

    public void setSetting(int setting) {
        this.setting = setting;
    }

    public Variable getRelated() {
        return related;
    }

    public void setRelated(Variable related) {
        this.related = related;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
