package pe.mm.reception.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**/

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH");
            }
        };
    }


    @Override

    protected void configure(HttpSecurity http) throws Exception {

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .csrf().disable().authorizeRequests().antMatchers( "/data/*","/reports","/login/recovery","/login/recovery/pass", "/login/recovery/verify")
                .permitAll()
                .antMatchers(HttpMethod.GET, "/", "/v2/api-docs", // swagger
						"/webjars/**", // swagger-ui webjars
						"/swagger-resources/**", // swagger-ui resources
						"/configuration/**", // swagger configuration
						"/*.html", "/favicon.ico", "/**/*.html", "/**/*.css", "/**/*.js")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .addFilter(new AuthorizationContextFilter(authenticationManager()));

    }


}
