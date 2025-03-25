package pokemon.splender.exception.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pokemon.splender.exception.CustomException;
import pokemon.splender.exception.ErrorMessage;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorMessage> handleCustomException(CustomException customException) {
        return new ResponseEntity<>(customException.getErrorMessage(), customException.getStatus());
    }
}
