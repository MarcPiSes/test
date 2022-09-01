package cat.udg.tfg.server.security;

import cat.udg.tfg.server.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenManager implements Serializable {
    private static final long serialVersionUID = 7008375124389347049L;
    @Value("${jwt.secretKey}")
    private String jwtSecret;

    public String generateJwtToken(User user, Date setTime, Long seconds) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder().setClaims(claims).setSubject(user.getUsername())
                .setIssuedAt(setTime)
                .setExpiration(new Date(System.currentTimeMillis() + seconds * 1000))
                .signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
    }

    public String generateJwtToken(User user, Date setTime) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder().setClaims(claims).setSubject(user.getUsername())
                .setIssuedAt(setTime)
                .signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
    }

    public Boolean validateJwtToken(String token, User user) {
        String username = getUsernameFromToken(token);
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        Boolean isTokenExpired = claims.getExpiration().before(new Date());
        Boolean isTokenSessionActive = compareIssuedDateTime(user, token);
        return (username.equals(user.getUsername()) && !isTokenExpired && isTokenSessionActive);
    }

    private Boolean compareIssuedDateTime(User user, String token) {
        if (getExpirationTimeFromToken(token) != null) {
            return user.getTokenDate().equals(
                    new Timestamp(getIssuedTimeFromToken(token).getTime()).toLocalDateTime()
            );
        }
        return user.getTokenPcDate().equals(
                new Timestamp(getIssuedTimeFromToken(token).getTime()).toLocalDateTime()
        );
    }

    public String getUsernameFromToken(String token) {
        final Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public Date getIssuedTimeFromToken(String token) {
        final Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return claims.getIssuedAt();
    }

    public Date getExpirationTimeFromToken(String token) {
        final Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return claims.getIssuedAt();
    }
}
