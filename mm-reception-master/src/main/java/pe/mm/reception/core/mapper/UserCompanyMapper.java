package pe.mm.reception.core.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import pe.mm.reception.core.model.CompanyBasic;
import pe.mm.reception.core.model.UserCompany;

import java.util.List;
@Mapper
public interface UserCompanyMapper {

    UserCompany getUserCompanyById(@Param("user_company_id") int id);

    UserCompany getUserCompanyWithUserEmails(@Param("user_company_id") int id);
    void insert(@Param("user_company") UserCompany userCompany, @Param("user_id") int idUser);



    void update(@Param("user_company")UserCompany userCompany, @Param("user_id") int idUser);

    void delete(@Param("user_company_id") int id, int deleteStatus, int idUser);

    void softDeleted(@Param("user_company_id") int id, @Param("deleted") int deleteStatus, @Param("user_id") int idUser);

    List<UserCompany> getAllByField(@Param("field") String field, @Param("value") String value);

    List<UserCompany> getAllByDocument(@Param("document_type_id") int documentTypeId, @Param("document_number") String documentNumber);

    List<UserCompany> getAll(@Param("startIndex") Integer startIndex, @Param("sizePage") Integer sizePage);

    void updateStatusCompany(@Param("user_company") UserCompany userCompany,@Param("user_id") int user_id);

    UserCompany getUserCompanyByIdWithImage(int id);

    List<CompanyBasic> getAllCompany();
}
