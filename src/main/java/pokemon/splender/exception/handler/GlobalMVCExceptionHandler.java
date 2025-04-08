package pokemon.splender.exception.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pokemon.splender.exception.CustomMVCException;
import pokemon.splender.exception.ErrorMessage;
import pokemon.splender.exception.ErrorResponse;

@RestControllerAdvice
public class GlobalMVCExceptionHandler {

    @ExceptionHandler(CustomMVCException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomMVCException customException) {
        ErrorMessage errorMessage = customException.getErrorMessage();
        ErrorResponse errorResponse = new ErrorResponse(
            errorMessage.getCode(),
            errorMessage.getMessage(),
            customException.getDetail()
        );

        return new ResponseEntity<>(errorResponse,
            customException.getStatus());
    }
}
