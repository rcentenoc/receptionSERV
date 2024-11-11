package pe.mm.reception.core.dto;

import pe.mm.reception.core.model.Line;

public class LineEssentialDTO {
    private int id;
    private String name;

    public LineEssentialDTO() {
    }

    public LineEssentialDTO(Line line) {
        id = line.getId();
        name = line.getName();
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
}
