package dsrl.energy.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
@RequiredArgsConstructor
public class Security {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Enable CORS and disable CSRF
        http.cors().and().csrf().disable();
        // Set session management to stateless
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Set unauthorized requests exception handler
       /* http.exceptionHandling(
                (exceptions) ->
                        exceptions
                                .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                                .accessDeniedHandler(new BearerTokenAccessDeniedHandler()));

        // Set permissions on endpoints
        http.authorizeRequests()
                // Swagger endpoints must be publicly accessible
                .antMatchers("/")
                .permitAll()
                .antMatchers(format("%s/**", restApiDocPath))
                .permitAll()
                .antMatchers(format("%s/**", swaggerPath))
                .permitAll()
                // Our public endpoints
                .antMatchers("/api/public/**")
                .permitAll()
                .antMatchers(HttpMethod.GET, "/api/author/**")
                .permitAll()
                .antMatchers(HttpMethod.POST, "/api/author/search")
                .permitAll()
                .antMatchers(HttpMethod.GET, "/api/book/**")
                .permitAll()
                .antMatchers(HttpMethod.POST, "/api/book/search")
                .permitAll()
                // Our private endpoints
                .anyRequest()
                .authenticated()
                // Set up oauth2 resource server
                .and()
                .httpBasic(Customizer.withDefaults())
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);

        */

        return http.build();
    }
}
