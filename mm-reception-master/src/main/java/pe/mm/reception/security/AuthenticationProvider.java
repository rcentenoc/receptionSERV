package pe.mm.reception.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import pe.mm.reception.security.dto.UserSessionDTO;
import pe.mm.reception.security.exception.JwtTokenMalformedException;
import pe.mm.reception.security.model.AuthenticatedToken;
import pe.mm.reception.security.model.AuthenticatedUser;

@Component
public class AuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    @Autowired
    private TokenParser jwtTokenValidator;

    @Override
    public boolean supports(Class<?> authentication) {
        return (AuthenticatedToken.class.isAssignableFrom(authentication));
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        AuthenticatedToken jwtAuthenticationToken = (AuthenticatedToken) authentication;
        String token = jwtAuthenticationToken.getToken();

        UserSessionDTO parsedUser = jwtTokenValidator.parseToken(token);

        if (parsedUser == null) {
            throw new JwtTokenMalformedException("Acceso no autorizado");
        }
        return new AuthenticatedUser(parsedUser, token);
    }

}