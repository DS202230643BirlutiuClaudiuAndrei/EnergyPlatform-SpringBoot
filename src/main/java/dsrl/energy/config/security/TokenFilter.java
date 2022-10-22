package dsrl.energy.config.security;

import dsrl.energy.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This component return the
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class TokenFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String token = extractToken(request);
        String username = null;
        try {
            username = token != null ? tokenProvider.getClaimFromToken(token, (Claims::getSubject)) : null;
        } catch (ExpiredJwtException e) {
            log.warn(String.format("Expired jwt token message: %s", e.getMessage()));
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails energyUser = userService.loadUserByUsername(username);

            if (tokenProvider.isValidToken(token, energyUser)) {
                UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(energyUser, null, energyUser.getAuthorities());
                upat.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                log.info("Set authentication in context holder");
                //set new authentication in spring security context
                SecurityContextHolder.getContext().setAuthentication(upat);
            }
        }
        filterChain.doFilter(request, response);

    }


    private String extractToken(HttpServletRequest request) {
        final String requestToken = request.getHeader("Authorization");
        if (requestToken == null) {
            log.warn("There is no Authorization header");
            return null;
        }
        //extract the token from "Bearer token"
        if (!requestToken.startsWith("Bearer ")) log.warn("Token does not begin with Bearer");
        return requestToken.substring(7);
    }
}
