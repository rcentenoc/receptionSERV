package pe.mm.reception.core.model;

import net.sf.oval.constraint.Email;
import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class UserCompany implements Serializable {

    @NotNull(profiles = {"update", "delete"})
    private int id;
    @NotNull(profiles = {"insert","update"})
    @NotEmpty(profiles = {"insert","update"})
    private String comertialName;
    @NotNull(profiles = {"insert","update"})
    @NotEmpty(profiles = {"insert","update"})
    private String information;

    private Date create;
    private Date update;

    @NotNull(profiles = {"insert","update"})
    @NotEmpty(profiles = {"insert","update"})
    private String denomination;
    @NotNull (profiles = {"insert","update"})
    private DocumentType documentType;
    @NotNull(profiles = {"insert","update"})
    @NotEmpty(profiles = {"insert","update"})
    private String documentNumber;
    @NotNull (profiles = {"insert","update"})
    @NotEmpty(profiles = {"insert","update"})
    private String address;
    @NotNull (profiles = {"insert","update"})
    @NotEmpty (profiles = {"insert","update"})
    private String phone1;
    @NotNull (profiles = {"insert","update"})
    @NotEmpty (profiles = {"insert","update"})
    private String phone2;
    @NotNull (profiles = {"insert","update"})
    @NotEmpty (profiles = {"insert","update"})
    private String phone3;
    @NotNull (profiles = {"insert","update"})
    @NotEmpty (profiles = {"insert","update"})
    private String website;
    @NotNull (profiles = {"insert","update"})
    @NotEmpty (profiles = {"insert","update"})
    private String agent;
    @NotNull (profiles = {"insert","update"})
    @NotEmpty (profiles = {"insert","update"})
    @Email(profiles = {"insert","update"})
    private String agentEmail;
    @NotNull(profiles = {"insert","update"})
    @NotEmpty(profiles = {"insert","update"})
    private String agentPhone;
    @NotNull(profiles = {"insert","update"})
    @NotEmpty (profiles = {"insert","update"})
    @Email(profiles = {"insert","update"})
    private String companyEmail;

    private Image image;
    private  int status;

    private List<User> users;



    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public UserCompany(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComertialName() {
        return comertialName;
    }

    public void setComertialName(String comertialName) {
        this.comertialName = comertialName;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public Date getCreate() {
        return create;
    }

    public void setCreate(Date create) {
        this.create = create;
    }

    public Date getUpdate() {
        return update;
    }

    public void setUpdate(Date update) {
        this.update = update;
    }

    public String getDenomination() {
        return denomination;
    }

    public void setDenomination(String denomination) {
        this.denomination = denomination;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getPhone3() {
        return phone3;
    }

    public void setPhone3(String phone3) {
        this.phone3 = phone3;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getAgentEmail() {
        return agentEmail;
    }

    public void setAgentEmail(String agentEmail) {
        this.agentEmail = agentEmail;
    }

    public String getAgentPhone() {
        return agentPhone;
    }

    public void setAgentPhone(String agentPhone) {
        this.agentPhone = agentPhone;
    }

    public String getCompanyEmail() {
        return companyEmail;
    }

    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


}
