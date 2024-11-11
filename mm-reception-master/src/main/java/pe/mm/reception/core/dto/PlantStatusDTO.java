package pe.mm.reception.core.dto;

import pe.mm.reception.core.model.PlantStatus;

public class PlantStatusDTO {

    private int id;
    private int status;
    private PlantEssentialDTO plant;

    public PlantStatusDTO(){

    }

    public PlantStatusDTO(PlantStatus plantStatus) {
        status = plantStatus.getStatus();
        plant = new PlantEssentialDTO(plantStatus.getPlant());
        id = plantStatus.getId();
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public PlantEssentialDTO getPlant() {
        return plant;
    }

    public void setPlant(PlantEssentialDTO plant) {
        this.plant = plant;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
