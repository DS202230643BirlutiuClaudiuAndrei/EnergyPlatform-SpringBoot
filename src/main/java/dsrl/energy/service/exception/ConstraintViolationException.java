package dsrl.energy.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class ConstraintViolationException extends RuntimeException {

    public ConstraintViolationException(String message) {
        super(message);
    }
}

