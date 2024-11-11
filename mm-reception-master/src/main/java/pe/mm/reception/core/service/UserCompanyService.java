package pe.mm.reception.core.service;


import pe.mm.reception.common.Request;
import pe.mm.reception.core.dto.UserCompanyUpdateStatusDTO;
import pe.mm.reception.core.model.CompanyBasic;
import pe.mm.reception.core.model.UserCompany;

import java.util.List;

public interface UserCompanyService {
    public UserCompany getUserCompany(int id);

    public UserCompany insert(UserCompany userCompany, int idUser);

    UserCompany update(UserCompany userCompany, int idUser);

    void delete(int id,int idUser);

    public List<UserCompany> getUsers();

    public boolean verifySimilarComertialName(UserCompany userCompany);

    public boolean verifySimilarDenomination(UserCompany userCompany);

    public boolean verifySimilarDocument(UserCompany userCompany);

    boolean verifySimilarComertialNameDistinctMe(UserCompany userCompany);

    boolean verifySimilarDenominationDistinctMe(UserCompany userCompany);

    boolean verifySimilarDocumentDistinctMe(UserCompany userCompany);

    List<UserCompany> getUsers(Request.Pager pager);

    void updateStatus(int id, UserCompanyUpdateStatusDTO userCompany);

    UserCompany getUserCompanyWithImage(int id);

    List<CompanyBasic> getAllCompany();
}
