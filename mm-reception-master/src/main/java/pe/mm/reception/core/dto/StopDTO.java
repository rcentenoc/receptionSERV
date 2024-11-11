package pe.mm.reception.core.dto;

import java.util.Date;

public class StopDTO {

    private int time;
    private int stopTypeId;
    private int legendId;
    private int stopTimeId;
    private int variableSelectedId;
    private Date date;

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getStopTypeId() {
        return stopTypeId;
    }

    public void setStopTypeId(int stopTypeId) {
        this.stopTypeId = stopTypeId;
    }

    public int getLegendId() {
        return legendId;
    }

    public void setLegendId(int legendId) {
        this.legendId = legendId;
    }

    public int getStopTimeId() {
        return stopTimeId;
    }

    public void setStopTimeId(int stopTimeId) {
        this.stopTimeId = stopTimeId;
    }

    public int getVariableSelectedId() {
        return variableSelectedId;
    }

    public void setVariableSelectedId(int variableSelectedId) {
        this.variableSelectedId = variableSelectedId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
