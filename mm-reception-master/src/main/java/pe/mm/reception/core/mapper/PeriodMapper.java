package pe.mm.reception.core.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import pe.mm.reception.core.model.RatePeriod;

import java.util.Date;
import java.util.List;

@Mapper
public interface PeriodMapper {
    List<RatePeriod> getAllByField(@Param("field") String field, @Param("value")String value);
    List<RatePeriod> getAllByPlant(@Param("plant_id") int idPlant);
    List<RatePeriod> getAllByDescription(@Param("description") String description,@Param("idTariff") int idTariff);
    List<RatePeriod> getAllByTariff(@Param("idTariff") int idTariff);
    void insert(@Param("rate_period") RatePeriod ratePeriod,@Param("user_id") int idUser);
    void update(@Param("rate_period") RatePeriod ratePeriod,@Param("user_id") int idUser);

    RatePeriod getById(@Param("period_id") int idPeriod);

    void softDeleted(@Param("period_id") int idPeriod, @Param("deleted") int statusDeleted, @Param("user_id") int idUser);
    void updatePlant(@Param("idPeriod") int idPeriod, @Param("idPlant") int idPlant, @Param("user_id") int idUser);
    void updateNULLByPlant(@Param("idPeriod") int idPeriod, @Param("user_id") int idUser);
    Integer getIdByDateRange(@Param("plant_id") int idPlant,
                             @Param("tariff_structure_id") int idTariff,
                             @Param("create") Date create);
}
