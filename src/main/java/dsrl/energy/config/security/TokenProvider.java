package dsrl.energy.config.security;

import dsrl.energy.config.general.TokenProperties;
import dsrl.energy.config.security.exception.InvalidToken;
import dsrl.energy.model.entity.EnergyUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@RequiredArgsConstructor
@Component
public class TokenProvider implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final TokenProperties tokenProperties;

    /**
     * This method construct the token at authentication
     *
     * @param energyUser the energy user who is authenticated
     * @return a string which represents the token generated
     */
    public String provideToken(EnergyUser energyUser) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("firstName", energyUser.getFirstName());
        claims.put("lastName", energyUser.getLastName());
        claims.put("id", energyUser.getId());
        claims.put("role", energyUser.getRole().name());
        Date issuedDate = new Date(System.currentTimeMillis());
        Date expirationDate = new Date(System.currentTimeMillis() + tokenProperties.getTokenValidity());
        return Jwts.builder().setClaims(claims).setSubject(energyUser.getUsername()).setIssuedAt(issuedDate).setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, tokenProperties.getTokenSecret()).compact();
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsTFunction) {
        Claims claims = null;
        try {
            claims = Jwts.parser().setSigningKey(tokenProperties.getTokenSecret()).parseClaimsJws(token).getBody();
        } catch (MalformedJwtException ex) {
            throw new InvalidToken(ex.getMessage());
        }

        return claimsTFunction.apply(claims);
    }

    /**
     * This method check if the token is valid
     *
     * @param token      the token extract rom http request header
     * @param energyUser the user from db
     * @return a boolean response based on condition of username and expiration date
     */
    public boolean isValidToken(String token, UserDetails energyUser) {
        String usernameFromToken = getClaimFromToken(token, Claims::getSubject);
        Date expirationDateFromToken = getClaimFromToken(token, Claims::getExpiration);
        return (expirationDateFromToken.after(new Date(System.currentTimeMillis()))) && usernameFromToken.equals(energyUser.getUsername());
    }

}
