package pokemon.splender.auth.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pokemon.splender.exception.CustomFilterException;
import pokemon.splender.jwt.service.RefreshTokenService;
import pokemon.splender.jwt.util.CookieUtil;
import pokemon.splender.jwt.util.JwtUtil;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    public void reissueToken(String refreshToken, HttpServletResponse response) {
        // 유효한 토큰인지 확인
        jwtUtil.validateToken(refreshToken);
        Long userId = getUserId(refreshToken);
        validateStoredToken(refreshToken, userId);
        reissueRefreshToken(response, userId);
    }

    public void logout(String refreshToken, HttpServletResponse response) {
        // 유효한 토큰인지 확인
        jwtUtil.validateToken(refreshToken);
        Long userId = getUserId(refreshToken);
        deleteRefreshTokenAndCookie(response, userId);
    }

    // 토큰에서 userId 추출
    private Long getUserId(String token) {
        return Long.valueOf(jwtUtil.getSubject(token));
    }

    private void validateStoredToken(String refreshToken, Long userId) {
        // Redis에 저장된 Refresh Token 가져오기
        String savedRefreshToken = refreshTokenService.getRefreshToken(userId);

        // Redis에 저장된 값과 비교
        if (!refreshToken.equals(savedRefreshToken)) {
            // 저장된 Refresh Token과 새로 들어온 Refresh Token이 다르면
            // 토큰 탈취 가능성이 있으므로
            // 저장된 토큰을 삭제 후 예외 던지기
            refreshTokenService.deleteRefreshToken(userId);
            throw CustomFilterException.invalidTokenException();
        }
    }

    private void reissueRefreshToken(HttpServletResponse response, Long userId) {
        // 새로운 토큰 생성
        String newAccessToken = jwtUtil.createAccessToken(userId);
        String newRefreshToken = jwtUtil.createRefreshToken(userId);
        refreshTokenService.saveRefreshToken(userId, newRefreshToken,
            jwtUtil.getRefreshTokenExpiration());

        // 쿠키 세팅
        response.addHeader("Set-Cookie",
            CookieUtil.createAccessTokenCookie(newAccessToken).toString());
        response.addHeader("Set-Cookie",
            CookieUtil.createRefreshTokenCookie(newRefreshToken).toString());

    }

    private void deleteRefreshTokenAndCookie(HttpServletResponse response, Long userId) {
        // Redis에서 삭제
        refreshTokenService.deleteRefreshToken(userId);

        response.addHeader("Set-Cookie", CookieUtil.deleteAccessTokenCookie().toString());
        response.addHeader("Set-Cookie", CookieUtil.deleteRefreshTokenCookie().toString());
    }

}
