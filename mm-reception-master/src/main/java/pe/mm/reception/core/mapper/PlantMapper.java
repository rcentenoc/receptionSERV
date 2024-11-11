package pe.mm.reception.core.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import pe.mm.reception.core.model.Plant;

import java.util.List;
@Mapper
public interface PlantMapper {
    List<Plant> getAllByCompany(@Param("user_company_id") int idCompany, @Param("startIndex") Integer startIndex, @Param("sizePage") Integer sizePage);
    Plant getById(@Param("plant_id") int id);
    void insert(@Param("plant") Plant plant, @Param("user_id") int idUser);
    void update(@Param("plant") Plant plant, @Param("user_id") int idUser);
    void delete(@Param("plant_id") int id);
    void softDeleted(@Param("plant_id") int id, @Param("deleted") int deleteStatus, @Param("user_id") int idUser);
    List<Plant> getAllByField(@Param("field") String field, @Param("value") String value, @Param("user_company_id") int idCompany);
    void createTableForPlant(@Param("plant_id") int plant_id);
    List<String> existNameTable(@Param("nameTable") String nameTable);
}
