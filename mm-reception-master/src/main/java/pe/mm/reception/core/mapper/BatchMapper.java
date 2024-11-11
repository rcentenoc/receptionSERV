package pe.mm.reception.core.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import pe.mm.reception.core.model.Batch;

import java.util.Date;
import java.util.List;
@Mapper
public interface BatchMapper {
    Batch getById(@Param("batch_id") int id);
    List<Batch> getAllByLineDateRange(@Param("line_id") int idLine,
                                      @Param("start") Date start,
                                      @Param("end") Date end);
    List<Batch> getAllByLineDateRangeWithEndNull(@Param("line_id") int idLine,
                                                 @Param("start") Date start,
                                                 @Param("end") Date end);
    void insert(@Param("batch")Batch batch,@Param("user_id") int user_id);
    void update(@Param("batch") Batch batch, @Param("user_id") int user_id);
    void softDeleted(@Param("batch_id") int id, @Param("delete") int deleteStatus, @Param("user_id") int idUser);
}
