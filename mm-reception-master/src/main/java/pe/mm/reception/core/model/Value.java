package pe.mm.reception.core.model;

public class Value {
    private Object x;
    private String y;
    private String value;
    private String caption;
    private Integer idTurn;

    public Value(String x, String y, String value, String caption) {
        this.x = x;
        this.y = y;
        this.value = value;
        this.caption = caption;
    }

    public Value() {
    }
/*
    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }
    /**/

    public String getY() {
        return y==null?"0":y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Object getX() {
        return x;
    }

    public void setX(Object x) {
        this.x = x;
    }

    public Integer getIdTurn() {
        return idTurn;
    }

    public void setIdTurn(Integer idTurn) {
        this.idTurn = idTurn;
    }
}
