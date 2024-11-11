package pe.mm.reception.core.model;

import java.util.Date;

public class Turn {
    private int id;
    private Line line;
    private String name;
    private Date create;
    private Date update;
    private int patternTurnId;
    private Patterncustom pattern;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getUpdate() {
        return update;
    }

    public void setUpdate(Date update) {
        this.update = update;
    }

    public Date getCreate() {

        return create;
    }

    public void setCreate(Date created) {
        this.create = created;
    }

    public int getPatternTurnId() {
        return patternTurnId;
    }

    public void setPatternTurnId(int patternTurnId) {
        this.patternTurnId = patternTurnId;
    }
    public Patterncustom getPattern() {
        return pattern;
    }

    public void setPattern(Patterncustom pattern) {
        this.pattern = pattern;
    }
}
