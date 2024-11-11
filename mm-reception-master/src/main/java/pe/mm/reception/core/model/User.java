package pe.mm.reception.core.model;

import net.sf.oval.constraint.Email;
import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;
import net.sf.oval.constraint.Size;
import pe.mm.reception.core.dto.PlantLoginDTO;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class User implements Serializable {
    @NotNull(profiles = {"update","delete"})
    private int id;


    private UserCompany userCompany;
    @Size(min=2, max=50)
    private String username;
    @Size(min=2, max=60)
    private String password;
    @Size(min=2, max=100)
    private String name;
    private Date create;
    private Date update;


    @Email(profiles = {"update","insert"},message = "Ingrese un email Válido")
    @NotNull(profiles = {"update","insert"})
    @NotEmpty(profiles = {"update","insert"})
    /**/
    private String email;


    @NotNull(profiles = {"update","insert"})
    @NotEmpty(profiles = {"update","insert"})
    /**/
    private String document;

    private DocumentType documentType;

    private int usermod;

    private List<Rol> roles;

    private int status;
    private PlantLoginDTO plant;
    public User() {
    }

    public int getUsermod() {
        return usermod;
    }

    public PlantLoginDTO getPlant() {
        return plant;
    }

    public void setPlant(PlantLoginDTO plant) {
        this.plant = plant;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public UserCompany getUserCompany() {
        return userCompany;
    }

    public void setUserCompany(UserCompany userCompany) {
        this.userCompany = userCompany;
    }

    public void setUsermod(int usermod) {
        this.usermod = usermod;
    }
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Rol> getRoles() {
        return roles;
    }

    public void setRoles(List<Rol> roles) {
        this.roles = roles;
    }

}
