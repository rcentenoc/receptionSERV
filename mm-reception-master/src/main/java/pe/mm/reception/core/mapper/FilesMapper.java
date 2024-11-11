package pe.mm.reception.core.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import pe.mm.reception.core.model.Files;

import java.util.List;
@Repository
@Mapper
public interface FilesMapper {
    void insert(@Param("files") Files files, @Param("user_id") int idUser);
    void update(@Param("files") Files files, @Param("user_id") int idUser);
    List<Files> getAllByCompany(@Param("user_company_id") int idCompany, @Param("startIndex") Integer startIndex, @Param("sizePage") Integer sizePage);
    List<Files> getAllByPlant(@Param("plant_id") int idPlant,@Param("startIndex") Integer startIndex, @Param("sizePage") Integer sizePage);
    //List getListFilesByCompany(@Param("plant_id") int idPlant);
    List<Files> getListFilesByLine(@Param("line_id") int idLine);
    List getAllByField(@Param("field") String field,@Param("files") Files files);
    void softDeleted(@Param("file_id") int file_id, @Param("deleted") int statusDeleted, @Param("user_id") int idUser);
    Files getById(@Param("file_id") int file_id);
}
