package pe.mm.reception.core.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import pe.mm.reception.core.model.GraphicVariable;
import pe.mm.reception.core.model.Value;

import java.util.Date;
import java.util.List;

/**
 * Created by César Calle on 26/08/2016.
 */
@Mapper
public interface GraphicVariableMapper {
    GraphicVariable getById(@Param("graphic_id") int idGraphic, @Param("variable_id") int idVariable);

    List<GraphicVariable> getAllByGraphic(@Param("graphic_id") int idGraphic);

    /**
     * @param type 1 = minutes, 2 = format
     * @param idVariabe
     * @param start
     * @param end
     * @param operation
     * @param group when is Integer that is minutes, when is String that is date format string.
     * @param table
     * @param timeZoneDiff
     * @return List of values
     */
    List<Value> getDataOperation(@Param("type") int type,
                                 @Param("variable_id") int idVariabe,
                                 @Param("start_date") Date start,
                                 @Param("end_date") Date end,
                                 @Param("operation") String operation,
                                 @Param("group") Object group,
                                 @Param("table_name") String table,
                                 @Param("time_zone_diff") int timeZoneDiff);

    List<Value> getDataOperationByTariffStruct(@Param("type") int type,
                                               @Param("variable_id") int idVariable,
                                               @Param("tariff_structure_id") int idTarifStruct,
                                               @Param("plant_id") int idPlant,
                                               @Param("start_date") Date start,
                                               @Param("end_date") Date end,
                                               @Param("operation") String operation,
                                               @Param("group") Object group,
                                               @Param("table_name") String tableName,
                                               @Param("table_index_name") String indexName,
                                               @Param("time_zone_diff") int timeZoneDiff);

    List<Value> getDataOperationByTurn(@Param("type") int type,
                                       @Param("variable_id") int idVariable,
                                       @Param("turn_id") int idTarifStruct,
                                       @Param("start_date") Date start,
                                       @Param("end_date") Date end,
                                       @Param("operation") String operation,
                                       @Param("group") Object group,
                                       @Param("table_name") String tableName,
                                       @Param("table_index_name") String indexName,
                                       @Param("time_zone_diff") int timeZoneDiff);

    List<Value> getDataOperationByRatePeriod(@Param("type") int type,
                                             @Param("variable_id") int idVarible,
                                             @Param("tariff_id") int idTariffStructure,
                                             @Param("rate_period_id") int idRatePeriod,
                                             @Param("start_date") Date start,
                                             @Param("end_date") Date end,
                                             @Param("operation") String operation,
                                             @Param("group") Object group,
                                             @Param("table_name") String table,
                                             @Param("table_index_name") String tableIndex,
                                             @Param("time_zone_diff") int timeZoneDiff);

    List<Value> getIndexByDates(@Param("index") String indexColumnName,
                                @Param("start_date") Date start,
                                @Param("end_date") Date end,
                                @Param("table_name") String table,
                                @Param("table_index_name") String tableIndex);

    List<Value> getIndexByTurnDates(@Param("index") String indexColumnName,
                                    @Param("turn_id") int idTurn,
                                    @Param("start_date") Date start,
                                    @Param("end_date") Date end,
                                    @Param("table_name") String table,
                                    @Param("table_index_name") String tableIndex);

    List<Value> getIndexByTariffStructureDates(@Param("index") String indexColumnName,
                                               @Param("tariff_structure_id") int idTariffStructure,
                                               @Param("start_date") Date start,
                                               @Param("end_date") Date end,
                                               @Param("table_name") String table,
                                               @Param("table_index_name") String tableIndex);

    void insert(@Param("graphicVariable") GraphicVariable graphicVariable, @Param("user_id") int idUser);

    void delete(@Param("graphic_id") int idGraphic,
                @Param("variable_id") int idVariable);

    List<GraphicVariable> getAllByGraphicLineOeeRealTime(@Param("graphic_id") int graphic_id,
                                                         @Param("line_id") int line_id);


}
