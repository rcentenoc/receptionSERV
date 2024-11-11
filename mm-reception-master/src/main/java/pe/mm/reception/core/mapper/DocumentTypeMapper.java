package pe.mm.reception.core.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import pe.mm.reception.core.model.DocumentType;

import java.util.List;

@Mapper
public interface DocumentTypeMapper {
    DocumentType getById(@Param("document_type_id") int id);
    void insert(@Param("document_type") DocumentType documentType,@Param("user_id") int idUser);
    void update(@Param("document_type") DocumentType documentType,@Param("user_id") int idUser);
    void delete(@Param("document_type_id") int id);
    void softDeleted(@Param("document_type_id") int id,@Param("deleted") int deleteState,@Param("user_id") int idUser);
    List<DocumentType> getAllByField(@Param("field") String field, @Param("value")String value);

    List<DocumentType> getAll(@Param("startIndex") Integer startIndex, @Param("sizePage") Integer sizePage);
}
