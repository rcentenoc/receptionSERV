package pe.mm.reception.core.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import pe.mm.reception.core.dto.StopDTO;
import pe.mm.reception.core.dto.WhereStamen;
import pe.mm.reception.core.model.Variable;

import java.util.Date;
import java.util.List;
import java.util.Map;
@Repository
@Mapper
public interface TableMapper {
        /**
         *
         * @param table
         * @param columns
         * @param values
         */
        void insert(@Param("table") String table, @Param("columns") List columns, @Param("values") List values);

        /**
         *
         * @param table
         * @param values
         * @param fieldId
         * @param idData
         */
        void update(@Param("table") String table, @Param("values") Map values, @Param("field_id") String fieldId,
                    @Param("id") String idData);

        /**
         *
         * @param table
         * @param values
         * @param wheres es una lista de lista de String que representan una sentencia
         *               en where.
         */
        void updateByCondition(@Param("table") String table, @Param("values") Map values,
                               @Param("comparatives") List<WhereStamen> wheres);

        /**
         *
         * @param field
         * @param create
         * @param table
         * @return
         */
        String getFieldByCreate(@Param("field") String field, @Param("create") Date create, @Param("table") String table);

        /**
         *
         * @param field
         * @param create
         * @param table
         * @return
         */
        List getFieldsByCreate(@Param("fields") List<String> field, @Param("create") Date create,
                               @Param("table") String table);

        /**
         *
         * @param field
         * @param create
         * @param table
         * @return
         */
        String getFieldByBeforeCreate(@Param("field") String field, @Param("create") Date create,
                                      @Param("table") String table);

        /**
         *
         * @param get
         * @param value
         * @param field
         * @param table
         * @return
         */
        String getFieldByOtherField(@Param("get") String get, @Param("value") String value, @Param("field") String field,
                                    @Param("table") String table);

        /**
         *
         * @param value
         * @param field
         * @param table
         * @return
         */
        List<Map<String, Object>> getAllFieldByOtherField(@Param("value") String value, @Param("field") String field,
                                                          @Param("table") String table);

        /**
         * Genera un select dinámico
         *
         * @param fields es una lista de String, en el que van los campos que se desean
         *               seleccionar
         * @param wheres es una lista de lista de String que representan una sentencia
         *               en where.
         * @param table  es el nombre de la tabla
         * @param limit  es el limite de registros que selecciona, puede ser null o 0
         *               para evitarse
         * @return una lista de map, cada posición en la lista es un registro y cada
         *         columna una posición en el map
         */
        List<Map<String, Object>> getCustomDynamic(@Param("fields_to_select") List<String> fields,
                                                   @Param("comparatives") List<WhereStamen> wheres, @Param("table") String table,
                                                   @Param("limit") Integer limit);

        /**
         * Genera un select dinámico sin comillas simples
         *
         * @param fields  es una lista de String, en el que van los campos que se desean
         *                seleccionar sin comillas simples
         * @param wheres  es una lista de lista de String que representan una sentencia
         *                en where.
         * @param table   es el nombre de la tabla
         * @param orderBy es el ordenamiento, puede ser null o "" para evitarse
         * @param limit   es el limite de registros que selecciona, puede ser null o 0
         *                para evitarse
         * @return una lista de map, cada posición en la lista es un registro y cada
         *         columna una posición en el map
         */
        List<Map<String, Object>> getDynamic(@Param("fields_to_select") List<String> fields,
                                             @Param("comparatives") List<WhereStamen> wheres, @Param("table") String table,
                                             @Param("orderBy") String orderBy, @Param("limit") Integer limit);

        /**
         * Genera un select dinámico sin comillas simples
         *
         * @param fields  es una lista de String, en el que van los campos que se desean
         *                seleccionar sin comillas simples
         * @param wheres  es una lista de lista de String que representan una sentencia
         *                en where.
         * @param tables   es el nombre de la tabla
         * @param orderBy es el ordenamiento, puede ser null o "" para evitarse
         * @param limit   es el limite de registros que selecciona, puede ser null o 0
         *                para evitarse
         * @return una lista de map, cada posición en la lista es un registro y cada
         *         columna una posición en el map
         */
        List<Map<String, Object>> getDynamicVarious(@Param("fields_to_select") List<String> fields,
                                                    @Param("comparatives") List<WhereStamen> wheres, @Param("tables") List<String> tables,
                                                    @Param("orderBy") String orderBy, @Param("limit") Integer limit);

        /**
         * inserta de manera multiple un conjunto de registros a una tabla, con columnas
         * dinámicas
         *
         * @param table   es el nombre de la tabla donde se va a guardar
         * @param columns es la lista de los nombres de las columnas
         * @param values  es la lista de lista de valores, cada lista en el segundo
         *                nivel debe tener el mismo tamaño que el de <tt>columns</tt>
         */
        void insertMultiple(@Param("table") String table, @Param("columns") List columns,
                            @Param("values") List<List<Object>> values);

        Object getMinFromTable(@Param("field") String field, @Param("table") String table);

        void updateDerivedOperationValue(@Param("table_continuing") String tableContinguing,
                                         @Param("table_derived") String tableDerived, @Param("values") Map<String, String> values,
                                         @Param("min_id") Integer minId, @Param("max_id") Integer maxId);

        void updateDerivedMixOperationValue(@Param("table_mix") String tableMix,
                                            @Param("parent1") String tableParent1,
                                            @Param("parent2") String tableParent2,
                                            @Param("values") Map<String, String> values,
                                            @Param("min_id") Integer minId, @Param("max_id") Integer maxId);

        void updateDerivedAcumValue(@Param("table1") String tableContinguing, @Param("table2") String tableDerived,
                                    @Param("values") Map<String, String> values, @Param("min_id") Integer minId,
                                    @Param("max_id") Integer maxId);

        void updateDerivedSpecialValue(@Param("table_continuing") String tableContinguing,
                                       @Param("table_derived") String tableDerived, @Param("table_index") String tableIndex,
                                       @Param("values") Map<String, String> values, @Param("min_id") Integer minId,
                                       @Param("max_id") Integer maxId);

        int updateStop(@Param("userId") int userId, @Param("stop") StopDTO stop , @Param("value") int value , @Param("selected_related") Variable selected_related , @Param("tablenameTipo") String tablenameTipo , @Param("tablenameTiempo") String tablenameTiempo , @Param("tablenameSelected") String tablenameSelected , @Param("tablenameSelectedRelated") String tablenameSelectedRelated);

}
