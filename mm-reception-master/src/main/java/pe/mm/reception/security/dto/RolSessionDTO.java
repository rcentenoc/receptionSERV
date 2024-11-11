package pe.mm.reception.security.dto;

import org.springframework.security.core.GrantedAuthority;

public class RolSessionDTO implements GrantedAuthority {

    private String description;

    RolSessionDTO(String description){
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getAuthority() {
        return description;
    }
}
