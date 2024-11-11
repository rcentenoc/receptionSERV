package pe.mm.reception.core.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface PagerMapper {
    int countPerTable(@Param("table_name") String table, @Param("field") String field, @Param("value") Object value);
}
