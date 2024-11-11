package pe.mm.reception.core.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import pe.mm.reception.core.model.Image;

@Mapper
public interface ImageMapper {
    Image getById(@Param("image_id") int id);

    void insert(@Param("image") Image img, @Param("user_id") int userId);

    void update(@Param("image") Image img, @Param("user_id") int userId);

    void delete(@Param("image_id") int id,@Param("user_id") int userId,@Param("deleted") int deleted);
}
