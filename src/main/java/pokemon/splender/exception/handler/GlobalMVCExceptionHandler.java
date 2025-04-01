package pokemon.splender.exception.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pokemon.splender.exception.CustomMVCException;
import pokemon.splender.exception.ErrorMessage;

@RestControllerAdvice
public class GlobalMVCExceptionHandler {

    @ExceptionHandler(CustomMVCException.class)
    public ResponseEntity<ErrorMessage> handleCustomException(CustomMVCException customException) {

        return new ResponseEntity<>(
            customException.getErrorMessage(),
            customException.getStatus());
    }
}
