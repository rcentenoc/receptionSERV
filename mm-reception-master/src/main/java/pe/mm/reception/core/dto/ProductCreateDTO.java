package pe.mm.reception.core.dto;

public class ProductCreateDTO {
    private String description;
    private String value;
    private String column;
    private String name;

    public ProductCreateDTO() {

    }

    public ProductCreateDTO(String description, String value, String column,String name) {
        this.description = description;
        this.value = value;
        this.column = column;
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
