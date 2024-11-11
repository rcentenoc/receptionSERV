package pe.mm.reception.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import pe.mm.reception.common.util.Dispar_Decode;
import pe.mm.reception.security.model.AuthenticatedToken;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class AuthorizationContextFilter extends BasicAuthenticationFilter {



    public AuthorizationContextFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(Constants.SECURITY.TOKEN_HEADER);
        if (header == null || !header.startsWith(Constants.SECURITY.START_TOKEN)) {
            chain.doFilter(req, res);
            return;
        }

        String token = req.getHeader(Constants.SECURITY.TOKEN_HEADER);
        String authToken = token.replace(Constants.SECURITY.START_TOKEN, "");
        Dispar_Decode codificador = new Dispar_Decode();
        AuthenticatedToken authRequest = new AuthenticatedToken(codificador.Decodificar(authToken));

        Authentication authentication=getAuthenticationManager().authenticate(authRequest);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(req, res);
    }

}