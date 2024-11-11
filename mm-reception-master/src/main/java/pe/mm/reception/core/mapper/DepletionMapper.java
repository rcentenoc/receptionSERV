package pe.mm.reception.core.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import pe.mm.reception.core.model.Calendary;
import pe.mm.reception.core.model.Depletion;

import java.util.Date;
import java.util.List;

@Mapper
public interface DepletionMapper {
    List<Depletion> getAllByTurn(@Param("turn_id") int turn_id,
                                 @Param("startIndex") Integer startIndex,
                                 @Param("sizePage") Integer sizePage);
    List<Depletion> getAllByLineRangeTime(@Param("line_id") int line_id,
                                          @Param("start") Date start,
                                          @Param("end") Date end);


    Depletion getById(@Param("depletion_id") int depletion_id);
    //List<Depletion> getByLine(@Param("line_id") int line_id,@Param("turn_id") int turn_id);
    void insert(@Param("depletion") Depletion depletion, @Param("user_id") int idUser);
    List<Depletion> getAllByField(@Param("field") String field, @Param("value") String value, @Param("turn_id") int turn_id);
    void update(@Param("depletion") Depletion depletion, @Param("user_id") int idUser);

    Depletion getByCalendary(@Param("range_time") Calendary rangeTime);
}
