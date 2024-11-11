package pe.mm.reception.core.dto;

import pe.mm.reception.core.model.Product;

import java.util.List;

public class ProductPropertyDTO {
    private int id;
    private LineEssentialDTO line;
    private String code;
    private String description;
    private Double ratedSpeed;
    private Double measurePerUnit;
    private List<ProductCreateDTO> listColumn;

    public ProductPropertyDTO() {

    }

    public ProductPropertyDTO(int id, LineEssentialDTO line, String code, String description, Double ratedSpeed, Double measurePerUnit, List<ProductCreateDTO> listColumn) {
        this.id = id;
        this.line = line;
        this.code = code;
        this.description = description;
        this.ratedSpeed = ratedSpeed;
        this.measurePerUnit = measurePerUnit;
        this.listColumn = listColumn;
    }

    public ProductPropertyDTO(Product product) {
        id = product.getId();
        line = new LineEssentialDTO(product.getLine());
        code = product.getCode();
        description = product.getDescription();
        ratedSpeed = product.getRatedSpeed();
        measurePerUnit = product.getMeasurePerUnit();
    }

    public LineEssentialDTO getLine() {
        return line;
    }

    public void setLine(LineEssentialDTO line) {
        this.line = line;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getRatedSpeed() {
        return ratedSpeed;
    }

    public void setRatedSpeed(Double ratedSpeed) {
        this.ratedSpeed = ratedSpeed;
    }

    public Double getMeasurePerUnit() {
        return measurePerUnit;
    }

    public void setMeasurePerUnit(Double measurePerUnit) {
        this.measurePerUnit = measurePerUnit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean esDecimal(String cad)
    {
        try
        {
            Double.parseDouble(cad);
            return true;
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
    }

    public List<ProductCreateDTO> getListColumn() {
        return listColumn;
    }

    public void setListColumn(List<ProductCreateDTO> listColumn) {
        this.listColumn = listColumn;
    }
}
