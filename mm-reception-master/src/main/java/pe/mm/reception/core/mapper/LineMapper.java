package pe.mm.reception.core.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import pe.mm.reception.core.dto.LineEssentialDTO;
import pe.mm.reception.core.model.Line;

import java.util.List;
@Repository
@Mapper
public interface LineMapper {
    Line getById(@Param("line_id") int id);
    List<Line> getAllByPlant(@Param("plant_id") int idPlant, @Param("startIndex") Integer startIndex, @Param("sizePage") Integer sizePage);
    List<Line> getAllByDefault(@Param("plant_id") int idPlant,@Param("startIndex") Integer startIndex, @Param("sizePage") Integer sizePage);
    List<Line> getAllByField(@Param("field") String field, @Param("value") String value, @Param("plant_id") int idPlant);
    void insert(@Param("line") Line line, @Param("user_id") int idUser);
    void update(@Param("line") Line line, @Param("user_id") int idUser);
    void delete(@Param("line_id") int id);
    void softDeleted(@Param("line_id") int id, @Param("deleted") int deleteStatus, @Param("user_id") int idUser);
    void createTrigger(@Param("plant_id") int idPlant, @Param("combination") String combination);
    List<LineEssentialDTO> getAllByPlantSimple(@Param("plant_id") int idPlant, @Param("startIndex") Integer startIndex, @Param("sizePage") Integer sizePage);

}
