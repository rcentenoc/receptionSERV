package pe.mm.reception.core.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import pe.mm.reception.core.model.Device;

import java.util.List;
@Mapper
public interface DeviceMapper {
    Device getById(@Param("device_id") int device_id);
    List<Device> getAllByLocation(@Param("location_id") int idLocation, @Param("startIndex") Integer startIndex, @Param("sizePage") Integer sizePage);
    List<Device> getAllByField(@Param("field") String field, @Param("value") String value, @Param("location_id") int idLocation);
    void insert(@Param("device") Device device, @Param("user_id") int idUser);
    void update(@Param("device") Device device, @Param("user_id") int idUser);
    void delete(@Param("device_id") int id);
    void softDeleted(@Param("device_id") int id, @Param("delete") int deleteStatus, @Param("user_id") int idUser);
}
