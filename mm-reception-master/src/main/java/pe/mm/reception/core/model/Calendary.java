package pe.mm.reception.core.model;

import java.util.Date;

public class Calendary {
    private int id;
    private Date started;
    private Date ended;
    private Date create;
    private Date update;
    private RatePeriod ratePeriod;
    private Turn turn;

    public Turn getTurn() {
        return turn;
    }

    public void setTurn(Turn turn) {
        this.turn = turn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getStarted() {
        return started;
    }

    public void setStarted(Date started) {
        this.started = started;
    }

    public Date getEnded() {
        return ended;
    }

    public void setEnded(Date ended) {
        this.ended = ended;
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

    public void setUpdate(Date updated) {
        this.update = updated;
    }

    public RatePeriod getRatePeriod() {
        return ratePeriod;
    }

    public void setRatePeriod(RatePeriod ratePeriod) {
        this.ratePeriod = ratePeriod;
    }
}
