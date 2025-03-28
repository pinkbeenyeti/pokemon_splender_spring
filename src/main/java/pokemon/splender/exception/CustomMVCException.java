package pokemon.splender.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static pokemon.splender.exception.ErrorMessage.INVALID_NAME_CHANGE_PERIOD;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class CustomMVCException extends RuntimeException {

    private final ErrorMessage errorMessage;
    private final HttpStatus status;

    public static CustomMVCException invalidNameChangePeriod() {
        return new CustomMVCException(INVALID_NAME_CHANGE_PERIOD, BAD_REQUEST);
    }
}
