package pe.mm.reception.core.model;

import net.sf.oval.constraint.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Report {
    @NotNull(profiles = {"update", "delete"})
    private int id;

    @NotNull(profiles = {"insert", "update"})
    @NotEmpty(profiles = {"insert", "update"})
    private String name;
    private Date create;
    private Date update;

    @NotNull(profiles = {"insert", "update"})
    private Plant plant;
    @NotNull(profiles = {"insert", "update"})
    @NotEmpty(profiles = {"insert", "update"})
    private String schedule;

    @Range(max = 1, min = 0, profiles = {"insert", "update"})
    private int status;

    @NotNull(profiles = {"insert", "update"})
    @CheckWith(value = CheckStartDate.class, message = "La fecha inicial no puede ser menor que 2000",
            profiles = {"insert", "update"})
    private Date startDate;
    @NotNull(profiles = {"insert", "update"})
    @CheckWith(value = CheckEndDate.class, message = "La fecha final no puede ser menor que la inicial",
            profiles = {"insert", "update"})
    private Date endDate;

    private Date lastStartDate;
    private Date lastEndDate;

    private int template;
    private Image image;
    private List<Graphic> graphics;


    public Report() {
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

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getLastStartDate() {
        return lastStartDate;
    }

    public void setLastStartDate(Date lastStartDate) {
        this.lastStartDate = lastStartDate;
    }

    public Date getLastEndDate() {
        return lastEndDate;
    }

    public void setLastEndDate(Date lastEndDate) {
        this.lastEndDate = lastEndDate;
    }


    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public List<Graphic> getGraphics() {
        return graphics;
    }

    public void setGraphics(List<Graphic> graphics) {
        this.graphics = graphics;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }


    public int getTemplate() {
        return template;
    }

    public void setTemplate(int template) {
        this.template = template;
    }

    public static class CheckEndDate implements CheckWithCheck.SimpleCheck{

        @Override
        public boolean isSatisfied(Object validatedObject, Object value) {
            Report report= (Report) validatedObject;
            Date endDate = (Date) value;
            return report.getStartDate().before(endDate);
        }
    }

    public static class CheckStartDate implements CheckWithCheck.SimpleCheck{

        @Override
        public boolean isSatisfied(Object validatedObject, Object value) {
            Report report= (Report) validatedObject;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            Integer anio = Integer.parseInt(sdf.format(report.getStartDate()));
            return anio>=2000;
        }
    }
}
