package pe.mm.reception.core.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import pe.mm.reception.core.model.Location;

import java.util.List;

@Mapper
public interface LocationMapper {
    Location getById(@Param("location_id") int id);
    List<Location> getAllByLine(@Param("line_id") int idLine, @Param("startIndex") Integer startIndex, @Param("sizePage") Integer sizePage);
    List<Location> getAllByField(@Param("field") String field, @Param("value") String value, @Param("line_id") int idLine);
    void insert(@Param("location") Location location, @Param("user_id") int idUser);
    void update(@Param("location") Location location, @Param("user_id") int idUser);
    void delete(@Param("location_id") int id);
    void softDeleted(@Param("location_id") int id, @Param("deleted") int deleteStatus, @Param("user_id") int idUser);
}
