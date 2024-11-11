package pe.mm.reception.core.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import pe.mm.reception.core.model.Plant;
import pe.mm.reception.core.model.PlantStatus;
import pe.mm.reception.core.model.StatusLog;
@Repository
@Mapper
public interface PlantStatusMapper {
    /**
     *
     * @param plant
     * @return
     */
    PlantStatus getStatusByPlant(@Param("plant") Plant plant);

    /**
     *
     * @param plantStatus
     * @param userId
     */
    void insertStatus(@Param("plant_status") PlantStatus plantStatus, @Param("user_id") int userId);

    /**
     *
     * @param plantStatus
     * @param userId
     */
    void updateStatus(@Param("plant_status") PlantStatus plantStatus, @Param("user_id") int userId);

    /**
     *
     * @param plantStatus
     * @return
     */
    StatusLog getLastLog(@Param("plant_status") PlantStatus plantStatus);

    /**
     *
     * @param statusLog
     * @param user_id
     */
    void insertStatusLog(@Param("status_log") StatusLog statusLog, @Param("user_id")  int user_id);

    /**
     *
     * @param statusLog
     * @param user_id
     */
    void updateStatusLog(@Param("status_log")StatusLog statusLog,@Param("user_id")  int user_id);
}
