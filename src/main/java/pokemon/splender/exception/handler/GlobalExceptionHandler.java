package pokemon.splender.exception.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pokemon.splender.exception.CustomException;
import pokemon.splender.exception.ErrorMessage;
import pokemon.splender.jwt.util.TokenCookieUtil;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorMessage> handleCustomException(CustomException customException) {
        HttpHeaders headers = new HttpHeaders();

        // 만료된 토큰이거나 탈취된 토큰일 경우 쿠키 삭제
        if (customException.getStatus() == HttpStatus.FORBIDDEN || customException.getStatus() == HttpStatus.UNAUTHORIZED) {
            headers.add("Set-Cookie", TokenCookieUtil.deleteAccessTokenCookie().toString());
            headers.add("Set-Cookie", TokenCookieUtil.deleteRefreshTokenCookie().toString());
        }

        return new ResponseEntity<>(
            customException.getErrorMessage(), // body : ErrorMessage enum 값
            headers,                           // headers : 쿠키 삭제 정보 포함
            customException.getStatus());      // status
    }
}
