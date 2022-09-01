package cat.udg.tfg.server.exceptions;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;

public class UserAlreadyExistAuthenticationException extends AuthenticationException {

    @Value("${authentication.username_exists}")
    private static String msg;

    public UserAlreadyExistAuthenticationException() {
        super(msg);
    }

}
