package dsrl.energy.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class DeleteException extends RuntimeException {

    public DeleteException(String message) {
        super(message);
    }
}