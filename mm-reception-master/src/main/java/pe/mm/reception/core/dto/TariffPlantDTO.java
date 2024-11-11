package pe.mm.reception.core.dto;

public class TariffPlantDTO {
    private int tariff_id;
    private int plant_id;

    public TariffPlantDTO() {
    }

    public TariffPlantDTO(int tariff_id, int plant_id) {
        this.tariff_id = tariff_id;
        this.plant_id = plant_id;
    }

    public int getTariff_id() {
        return tariff_id;
    }

    public void setTariff_id(int tariff_id) {
        this.tariff_id = tariff_id;
    }

    public int getPlant_id() {
        return plant_id;
    }

    public void setPlant_id(int plant_id) {
        this.plant_id = plant_id;
    }
}
