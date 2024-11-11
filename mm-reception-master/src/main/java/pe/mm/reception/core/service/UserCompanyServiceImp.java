package pe.mm.reception.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.mm.reception.common.Request;
import pe.mm.reception.core.dto.UserCompanyUpdateStatusDTO;
import pe.mm.reception.core.mapper.UserCompanyMapper;
import pe.mm.reception.core.model.CompanyBasic;
import pe.mm.reception.core.model.UserCompany;

import java.util.List;
@Service
public class UserCompanyServiceImp implements UserCompanyService {
    @Autowired
    private UserCompanyMapper userCompanyMapper;

    public UserCompany getUserCompany(int id){
        return userCompanyMapper.getUserCompanyById(id);
    }

    @Override
    public UserCompany insert(UserCompany userCompany, int idUser) {
        userCompanyMapper.insert(userCompany,idUser);
        return userCompany;
    }

    @Override
    public UserCompany update(UserCompany userCompany, int idUser){
        userCompanyMapper.update(userCompany, idUser);
        return  userCompany;
    }

    @Override
    public void delete(int id, int idUser){
        userCompanyMapper.softDeleted(id,2,idUser);
    }

    @Override

    public List<UserCompany> getUsers(){
        return userCompanyMapper.getAll(null,null);
    }

    @Override
    public boolean verifySimilarComertialName(UserCompany userCompany) {
        List<UserCompany> userCompanies = userCompanyMapper.getAllByField(
                "comertial_name", userCompany.getComertialName());
        return userCompanies.size() > 0;
    }

    @Override
    public boolean verifySimilarDenomination(UserCompany userCompany) {
        List<UserCompany> userCompanies = userCompanyMapper.getAllByField(
                "denomination", userCompany.getDenomination());
        return userCompanies.size() > 0;
    }

    @Override
    public boolean verifySimilarDocument(UserCompany userCompany) {
        List<UserCompany> userCompanies = userCompanyMapper.getAllByDocument(
                userCompany.getDocumentType().getId(), userCompany.getDocumentNumber());
        return userCompanies.size()>0;
    }

    @Override
    public boolean verifySimilarComertialNameDistinctMe(UserCompany userCompany) {
        List<UserCompany> userCompanies = userCompanyMapper.getAllByField(
                "comertial_name", userCompany.getComertialName());
        if(userCompanies.size()==1 && userCompanies.get(0).getId()==userCompany.getId())
            return false;
        return userCompanies.size() > 0;
    }

    @Override
    public boolean verifySimilarDenominationDistinctMe(UserCompany userCompany) {
        List<UserCompany> userCompanies = userCompanyMapper.getAllByField(
                "denomination", userCompany.getDenomination());
        if(userCompanies.size()==1 && userCompanies.get(0).getId()==userCompany.getId())
            return false;
        return userCompanies.size() > 0;
    }

    @Override
    public boolean verifySimilarDocumentDistinctMe(UserCompany userCompany) {
        List<UserCompany> userCompanies = userCompanyMapper.getAllByDocument(
                userCompany.getDocumentType().getId(), userCompany.getDocumentNumber());
        if(userCompanies.size()==1 && userCompanies.get(0).getId()==userCompany.getId())
            return false;
        return userCompanies.size()>0;
    }

    @Override
    public List<UserCompany> getUsers(Request.Pager pager) {
        return null;// userCompanyMapper.getAll(pager.getStartIndex(), pager.getPageSize());
    }

    @Override
    public void updateStatus(int user_id_mod , UserCompanyUpdateStatusDTO dto){
        UserCompany company = new UserCompany();
        company.setId(dto.getId());
        company.setStatus(dto.getStatus());
        userCompanyMapper.updateStatusCompany(company,user_id_mod);

    }

    @Override
    public UserCompany getUserCompanyWithImage(int id) {
        return userCompanyMapper.getUserCompanyByIdWithImage(id);
    }

    @Override
    public List<CompanyBasic> getAllCompany() {
        return userCompanyMapper.getAllCompany();
    }

}
