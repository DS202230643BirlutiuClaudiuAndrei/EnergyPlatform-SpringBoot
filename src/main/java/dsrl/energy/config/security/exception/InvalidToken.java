package dsrl.energy.config.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class InvalidToken extends RuntimeException {

    public InvalidToken(String message) {
        super(message);
    }
}
