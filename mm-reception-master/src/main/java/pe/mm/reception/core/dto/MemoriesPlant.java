package pe.mm.reception.core.dto;

import pe.mm.reception.core.model.Plant;
import pe.mm.reception.core.model.PlantStatus;

import java.io.Serializable;
import java.util.Map;

public class MemoriesPlant implements Serializable {
    private Plant plant;
    /**
     * <File name, memory of file>
     */
    private Map<String, Memory> files;
    /**
     * 0: desactivado
     * 1: activado
     */
    private PlantStatus plantStatus;

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    public Map<String, Memory> getFiles() {
        return files;
    }

    public void setFiles(Map<String, Memory> files) {
        this.files = files;
    }

    public PlantStatus getPlantStatus() {
        return plantStatus;
    }

    public void setPlantStatus(PlantStatus plantStatus) {
        this.plantStatus = plantStatus;
    }
}
