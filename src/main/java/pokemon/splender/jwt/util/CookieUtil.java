package pokemon.splender.jwt.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;
import pokemon.splender.exception.CustomFilterException;

public class CookieUtil {

    public static String getCookieValue(HttpServletRequest request, String name) {
        if (request.getCookies() == null) {
            return null;
        }

        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(name)) {
                return cookie.getValue();
            }
        }

        throw CustomFilterException.notExistThatCookie();
    }

    public static ResponseCookie createAccessTokenCookie(String accessToken) {
        return ResponseCookie.from("access_token", accessToken)
            .httpOnly(true)
            .secure(true)
            .path("/api") // 유효 경로 설정
            .sameSite("Strict") // 다른 도메인에 요청 시 쿠키 포함 불가
            .maxAge(60 * 30)  // 30분
            .build();
    }

    public static ResponseCookie createRefreshTokenCookie(String refreshToken) {
        return ResponseCookie.from("refresh_token", refreshToken)
            .httpOnly(true)
            .secure(true)
            .path("/api") // 유효 경로 설정
            .sameSite("Strict") // 다른 도메인에 요청 시 쿠키 포함 불가
            .maxAge(60 * 60 * 24 * 7)  // 7일
            .build();
    }

    public static ResponseCookie createNewUserCookie(boolean newUser) {
        return ResponseCookie.from("new_user", String.valueOf(newUser))
            .httpOnly(false)
            .secure(true)
            .path("/") // 유효 경로 설정
            .sameSite("Strict") // 다른 도메인에 요청 시 쿠키 포함 불가
            .maxAge(60 * 60 * 24)  // 1일
            .build();
    }

    public static ResponseCookie deleteAccessTokenCookie() {
        return ResponseCookie.from("access_token", "")
            .httpOnly(true)
            .secure(true)
            .path("/")
            .sameSite("Strict")
            .maxAge(0) // 즉시 만료
            .build();
    }

    public static ResponseCookie deleteRefreshTokenCookie() {
        return ResponseCookie.from("refresh_token", "")
            .httpOnly(true)
            .secure(true)
            .path("/")
            .sameSite("Strict")
            .maxAge(0) // 즉시 만료
            .build();
    }

}
