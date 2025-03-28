package pokemon.splender.jwt.util;

import org.springframework.http.ResponseCookie;

public class TokenCookieUtil {

    public static ResponseCookie createAccessTokenCookie(String accessToken) {
        return ResponseCookie.from("access_token", accessToken)
            .httpOnly(true)
            .secure(true)
            .path("/") // 유효 경로 설정
            .sameSite("Strict") // 다른 도메인에 요청 시 쿠키 포함 불가
            .maxAge(60 * 30)  // 30분
            .build();
    }

    public static ResponseCookie createRefreshTokenCookie(String refreshToken) {
        return ResponseCookie.from("refresh_token", refreshToken)
            .httpOnly(true)
            .secure(true)
            .path("/") // 유효 경로 설정
            .sameSite("Strict") // 다른 도메인에 요청 시 쿠키 포함 불가
            .maxAge(60 * 60 * 24 * 7)  // 7일
            .build();
    }

}
