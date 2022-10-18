package dsrl.energy.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resource, String field, String value) {
        super(String.format("%s by field %s = %s doesn't exist", resource, field, value));
    }
}
