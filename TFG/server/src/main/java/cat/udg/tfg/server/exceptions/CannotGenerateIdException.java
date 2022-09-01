package cat.udg.tfg.server.exceptions;

import org.springframework.beans.factory.annotation.Value;

public class CannotGenerateIdException extends RuntimeException {
    @Value("${authentication.username_exists}")
    private static String msg;

    public CannotGenerateIdException(String entityName) {
        super(String.format(msg, entityName));
    }
}
