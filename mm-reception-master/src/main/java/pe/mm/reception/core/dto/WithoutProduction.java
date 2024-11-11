package pe.mm.reception.core.dto;

import java.util.Date;

public class WithoutProduction {
    Date started;

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

    Date ended;
}
