package pe.mm.reception.core.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import pe.mm.reception.core.model.Calendary;
import pe.mm.reception.core.model.Line;

import java.util.Date;
import java.util.List;

@Mapper
public interface CalendaryMapper {

    public List<Calendary> getAllMonthByPlant(@Param("plant_id") int idPlant,
                                              @Param("year") int year,
                                              @Param("month") int month,
                                              @Param("time_zone_diff") long diffTime);
    public List<Calendary> getAllTurnByLineDateRange(@Param("line_id") int idLine,
                                                     @Param("start") Date start,
                                                     @Param("end") Date end);
    public List<Calendary> getAllByRatePeriodDateRange(@Param("rate_period_id") int idRatePeriod,
                                                       @Param("start") Date start,
                                                       @Param("end") Date end);

    public void insert(@Param("calendary") Calendary calendary, @Param("user_id") int idUser);

    void softDeltedByMonth(@Param("plant_id") int idPlant,
                           @Param("year") int year,
                           @Param("month") int month,
                           @Param("deleted") int statusDeleted,
                           @Param("user_id") int idUser);

    void softDelted(@Param("calendary_id") int idCalendary,
                    @Param("user_id") int idUser, @Param("deleted") int deleted);


    Calendary getById(@Param("calendary_id") int idCalendary);

    void update(@Param("calendary") Calendary calendary, @Param("user_id") int idUser);

    public List<Calendary> getRangeTimeOEERT(@Param("start") Date start, @Param("end") Date end, @Param("line_id") int line_id);

    Calendary getTurnRangeTimeByDate(@Param("line") Line line, @Param("date") Date date);
}
