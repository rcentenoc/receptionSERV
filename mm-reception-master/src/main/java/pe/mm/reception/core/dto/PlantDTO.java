package pe.mm.reception.core.dto;

import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;
import pe.mm.reception.core.model.Line;
import pe.mm.reception.core.model.UserCompany;

import java.util.Date;
import java.util.List;

public class PlantDTO {
    @NotNull(profiles = {"update"})
    private int id;
    @NotNull
    private UserCompany userCompany;
    @NotNull @NotEmpty
    @Length(max =100)
    private String name;
    @NotNull @NotEmpty @Length(max =100)
    private String information;
    private String timeZone;
    private List<Line> lines;
    private Date create;
    private Date update;

    public PlantDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserCompany getUserCompany() {
        return userCompany;
    }

    public void setUserCompany(UserCompany userCompany) {
        this.userCompany = userCompany;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
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

    public List<Line> getLines() {
        return lines;
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }
}
