package dsrl.energy.controller;

import dsrl.energy.config.security.AccessDeniedHandler;
import dsrl.energy.config.security.exception.InvalidToken;
import dsrl.energy.service.exception.ConstraintViolationException;
import dsrl.energy.service.exception.DeleteException;
import dsrl.energy.service.exception.ErrorDetails;
import dsrl.energy.service.exception.ResourceNotFoundException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.util.Date;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> resourceNotFoundException(ConstraintViolationException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(InvalidToken.class)
    public ResponseEntity<?> invalidToken(InvalidToken ex, WebRequest request){
        ErrorDetails errorDetails = new ErrorDetails(new Date(), "asdasd", request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> invalidToken(AccessDeniedException ex, WebRequest request){
        ErrorDetails errorDetails = new ErrorDetails(new Date(), "asdasd", request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DeleteException.class)
    public ResponseEntity<?> invalidToken(DeleteException ex, WebRequest request){
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<ObjectError> errs = ex.getBindingResult().getAllErrors();
        ErrorDetails errorDetails = ErrorDetails.builder().details("Not valid information").timestamp(new Date()).build();
        return handleExceptionInternal(
                ex,
                errorDetails,
                new HttpHeaders(),
                status,
                request
        );
    }

}
