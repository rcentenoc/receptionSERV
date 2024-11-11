package pe.mm.reception.core.model;

import java.util.Date;
import java.util.List;

public class Graphic {
    private int id;
    private Report report;
    private GraphicType type;
    private Date create;
    private Date update;
    private Date initial;
    private int range;
    private String group;
    private String name;
    private List<GraphicTime> times;
    private List<GraphicVariable> variables;
    private int turnId;
    private List<Integer> turns;

    public Graphic() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public GraphicType getType() {
        return type;
    }

    public void setType(GraphicType type) {
        this.type = type;
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

    public Date getInitial() {
        return initial;
    }

    public void setInitial(Date initial) {
        this.initial = initial;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GraphicTime> getTimes() {
        return times;
    }

    public void setTimes(List<GraphicTime> times) {
        this.times = times;
    }

    public List<GraphicVariable> getVariables() {
        return variables;
    }

    public void setVariables(List<GraphicVariable> variables) {
        this.variables = variables;
    }

    public int getTurnId() {
        return turnId;
    }

    public void setTurnId(int turnId) {
        this.turnId = turnId;
    }

    public List<Integer> getTurns() {
        return turns;
    }

    public void setTurns(List<Integer> turns) {
        this.turns = turns;
    }
}
