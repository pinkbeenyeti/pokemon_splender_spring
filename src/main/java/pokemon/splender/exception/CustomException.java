package pokemon.splender.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static pokemon.splender.exception.ErrorMessage.EXPIRED_TOKEN;
import static pokemon.splender.exception.ErrorMessage.INVALID_HEADER;
import static pokemon.splender.exception.ErrorMessage.INVALID_NAME_CHANGE_PERIOD;
import static pokemon.splender.exception.ErrorMessage.INVALID_OAUTH_FORMAT;
import static pokemon.splender.exception.ErrorMessage.INVALID_OAUTH_PROVIDER;
import static pokemon.splender.exception.ErrorMessage.INVALID_TOKEN;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class CustomException extends RuntimeException {

    private final ErrorMessage errorMessage;
    private final HttpStatus status;

    public static CustomException invalidHeaderException() {
        return new CustomException(INVALID_HEADER, BAD_REQUEST);
    }

    public static CustomException invalidNameChangePeriod() {
        return new CustomException(INVALID_NAME_CHANGE_PERIOD, BAD_REQUEST);
    }

    public static CustomException invalidOAuthFormatException() {
        return new CustomException(INVALID_OAUTH_FORMAT, INTERNAL_SERVER_ERROR);
    }

    public static CustomException invalidOAuthProviderException() {
        return new CustomException(INVALID_OAUTH_PROVIDER, BAD_REQUEST);
    }

    public static CustomException invalidTokenException() {
        return new CustomException(INVALID_TOKEN, UNAUTHORIZED);
    }

    public static CustomException expiredTokenException() {
        return new CustomException(EXPIRED_TOKEN, FORBIDDEN);
    }
}
