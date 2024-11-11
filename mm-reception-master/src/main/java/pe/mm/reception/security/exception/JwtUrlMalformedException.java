package pe.mm.reception.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Created by admin1 on 26/10/16.
 */
public class JwtUrlMalformedException extends AuthenticationException {
    public JwtUrlMalformedException(String msg) {
        super(msg);
    }
}
