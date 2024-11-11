package pe.mm.reception.core.model;

import java.util.Date;

public class GraphicTime {
    private int id;
    private Date initial;
    private Date end;
    private Date create;
    private Date update;

    private int idGraphic;

    public GraphicTime() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getInitial() {
        return initial;
    }

    public void setInitial(Date initial) {
        this.initial = initial;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
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
    public int getIdGraphic() {
        return idGraphic;
    }

    public void setIdGraphic(int idGraphic) {
        this.idGraphic = idGraphic;
    }
}
