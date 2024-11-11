package pe.mm.reception.security.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pe.mm.reception.security.dto.UserSessionDTO;

import java.util.Collection;

/**
 * Holds the info for a authenticated user (Principal)
 * @author pascal alma
 */
public class AuthenticatedUser implements UserDetails {

    private final int id;
    private final String username;
    private final UserSessionDTO user;
    private final String token;
    private final Collection<? extends GrantedAuthority> authorities;

    public AuthenticatedUser(UserSessionDTO userDto, String token) {
        this.id = userDto.getId();
        this.username = userDto.getName();
        this.token = token;
        this.authorities = userDto.getRoles();
        this.user = userDto;
    }


	@JsonIgnore
    public int getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public UserSessionDTO getUser() {return user;}


    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

    public String getToken() {
        return token;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }
}
