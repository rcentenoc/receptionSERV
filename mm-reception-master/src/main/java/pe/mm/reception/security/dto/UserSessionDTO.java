package pe.mm.reception.security.dto;



import java.util.ArrayList;
import java.util.List;

import static pe.mm.reception.security.Constants.SECURITY.ROLES_SEPARATOR;

public class UserSessionDTO {
    private int id;
    private UserCompanySessionDTO userCompany;
    private String name;
    private PlantSesisonDTO plant;

    private List<RolSessionDTO> roles;

    public UserSessionDTO(){

    }

    public PlantSesisonDTO getPlant() {
        return plant;
    }

    public void setPlant(PlantSesisonDTO plant) {
        this.plant = plant;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserCompanySessionDTO getUserCompany() {
        return userCompany;
    }

    public void setUserCompany(UserCompanySessionDTO userCompany) {
        this.userCompany = userCompany;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public List<RolSessionDTO> getRoles() {
        return roles;
    }

    public void setRoles(List<RolSessionDTO> roles) {
        this.roles = roles;
    }

    public void setRoles(String rolesAsList) {
        this.roles = new ArrayList<>();
        for(String role : rolesAsList.split(ROLES_SEPARATOR)){
            this.roles.add(new RolSessionDTO(role));
        }
    }
}
