package cat.udg.tfg.server.services;

import cat.udg.tfg.server.domain.User;
import cat.udg.tfg.server.security.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

@Service
@Configurable
public class TokenService {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenManager tokenManager;

    public String createToken(Long seconds, String userName, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
        } catch (DisabledException e) { //TODO exception handling
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        final User user = userService.loadUserByUsername(userName);
        Date setTime = new Date(System.currentTimeMillis());
        String token = null;
        if(seconds != null) {
            token = tokenManager.generateJwtToken(user, setTime, seconds);
            user.setTokenPcDate(new Timestamp(setTime.getTime()).toLocalDateTime());
        }
        else {
            token = tokenManager.generateJwtToken(user, setTime);
            user.setTokenDate(new Timestamp(setTime.getTime()).toLocalDateTime());
        }
        userService.save(user); //TODO exception handling
        return token;
    }
}
