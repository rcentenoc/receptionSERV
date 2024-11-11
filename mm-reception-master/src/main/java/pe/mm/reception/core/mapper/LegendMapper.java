package pe.mm.reception.core.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import pe.mm.reception.core.model.Legend;

import java.util.List;

/**
 * Created by César Calle on 14/09/2016.
 */
@Mapper
public interface LegendMapper {
    List<Legend> getAllByVariable(@Param("variable_id") int idVarible,
                                  @Param("startIndex") Integer startIndex,
                                  @Param("sizePage") Integer sizePage);

    Legend getById(@Param("legend_id") int idLegend);

    List<Legend> getAllByField(@Param("field") String field, @Param("value") String value, @Param("variable_id") int idVariable);

    void insert(@Param("legend") Legend legend, @Param("user_id") int idUser);
    void update(@Param("legend") Legend legend, @Param("user_id") int idUser);
    void softDeleted(@Param("legend_id") int idLegend, @Param("deleted") int statusDeleted, @Param("user_id") int idUser);
    Legend getByVariableandValue(@Param("variable_id") int idVarible, @Param("value") int value);
}
