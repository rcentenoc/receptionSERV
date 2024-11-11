package pe.mm.reception.core.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import pe.mm.reception.core.dto.TariffPlantDTO;
import pe.mm.reception.core.model.TariffStructure;

import java.util.List;

@Mapper
public interface TariffStructureMapper {

    TariffStructure getById(@Param("tariff_structure_id") int id);
    List<TariffStructure> getAll(@Param("startIndex") Integer startIndex,
                                 @Param("sizePage") Integer sizePage);
    void insert(@Param("tariff_structure") TariffStructure tariffStructure, @Param("user_id") int idUser );

    void update(@Param("tariff_structure")TariffStructure tariffStructure, @Param("user_id") int idUser);
    void softDeleted(@Param("tariff_structure_id") int idPeriod, @Param("deleted") int statusDeleted, @Param("user_id") int idUser);

    List<TariffStructure> getAllByPlant(@Param("plant_id") int plant_id);
    List<Integer> getAllTariffByPlant(@Param("tariff_structure_id") int tariff_structure_id);
    List<TariffPlantDTO> getAllTariffPlantByPlant(@Param("plant_id") int plant_id);
    void deletePlantOfTariffPlant (@Param("tariff_id")int tariff_id,@Param("plant_id")int plant_id);
    void insertTariffStructurehasPlant(@Param("idTariff")int idTariff,@Param("idPlant")int idPlant);
}
