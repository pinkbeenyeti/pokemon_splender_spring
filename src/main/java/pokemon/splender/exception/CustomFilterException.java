package pokemon.splender.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static pokemon.splender.exception.ErrorMessage.EMPTY_COOKIES;
import static pokemon.splender.exception.ErrorMessage.EXPIRED_TOKEN;
import static pokemon.splender.exception.ErrorMessage.INVALID_OAUTH_FORMAT;
import static pokemon.splender.exception.ErrorMessage.INVALID_OAUTH_PROVIDER;
import static pokemon.splender.exception.ErrorMessage.INVALID_TOKEN;
import static pokemon.splender.exception.ErrorMessage.NOT_EXIST_ACCESS_COOKIE;
import static pokemon.splender.exception.ErrorMessage.NOT_EXIST_REFRESH_COOKIE;
import static pokemon.splender.exception.ErrorMessage.NOT_EXIST_THAT_COOKIE;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class CustomFilterException extends RuntimeException {

    private final ErrorMessage errorMessage;
    private final HttpStatus status;

    public static CustomFilterException emptyCookiesException() {
        return new CustomFilterException(EMPTY_COOKIES, BAD_REQUEST);
    }

    public static CustomFilterException notExistAccessTokenCookie() {
        return new CustomFilterException(NOT_EXIST_ACCESS_COOKIE, BAD_REQUEST);
    }

    public static CustomFilterException notExistRefreshTokenCookie() {
        return new CustomFilterException(NOT_EXIST_REFRESH_COOKIE, BAD_REQUEST);
    }

    public static CustomFilterException notExistThatCookie() {
        return new CustomFilterException(NOT_EXIST_THAT_COOKIE, BAD_REQUEST);
    }

    public static CustomFilterException invalidOAuthFormatException() {
        return new CustomFilterException(INVALID_OAUTH_FORMAT, INTERNAL_SERVER_ERROR);
    }

    public static CustomFilterException invalidOAuthProviderException() {
        return new CustomFilterException(INVALID_OAUTH_PROVIDER, BAD_REQUEST);
    }

    public static CustomFilterException invalidTokenException() {
        return new CustomFilterException(INVALID_TOKEN, UNAUTHORIZED);
    }

    public static CustomFilterException expiredTokenException() {
        return new CustomFilterException(EXPIRED_TOKEN, FORBIDDEN);
    }
}
