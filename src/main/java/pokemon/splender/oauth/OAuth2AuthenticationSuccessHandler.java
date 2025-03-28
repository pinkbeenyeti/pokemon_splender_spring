package pokemon.splender.oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import pokemon.splender.config.properties.OAuth2Properties;
import pokemon.splender.exception.CustomFilterException;
import pokemon.splender.jwt.util.JwtUtil;
import pokemon.splender.user.entity.User;
import pokemon.splender.user.service.UserService;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final OAuth2Properties oAuth2Properties;

    private String providerId, provider;
    private boolean newUser = false;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {
        //사용자 정보 가져오기
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // OAuth2 정보 추출
        setOAuth2ProviderInfo(attributes);

        // 유저 확인 및 생성
        User user = userService.findByProviderIdAndProvider(providerId, provider)
            .orElseGet(() -> {
                newUser = true; // 새로운 사용자일 경우 true
                return userService.createUser(providerId, provider);
            });

        // jwt 토큰 생성
        String accessToken = jwtUtil.createAccessToken(user.getId());
        String refreshToken = jwtUtil.createRefreshToken(user.getId());

        // 쿠키에 토큰 저장
        setTokenCookies(response, accessToken, refreshToken);

        // 리다이렉트
        response.sendRedirect(oAuth2Properties.getRedirectDevUrl());
    }

    private void setOAuth2ProviderInfo(Map<String, Object> attributes) {
        if (attributes.containsKey("sub")) {  // Google OAuth
            providerId = (String) attributes.get("sub");
            provider = "google";
        } else if (attributes.containsKey("id")) {  // Kakao OAuth
            providerId = attributes.get("id").toString();
            provider = "kakao";
        } else {
            throw CustomFilterException.invalidOAuthProviderException();  // 두 가지 모두 없을 경우
        }
    }

    private void setTokenCookies(HttpServletResponse response, String accessToken,
        String refreshToken) {
        ResponseCookie accessTokenCookie = ResponseCookie.from("access_token", accessToken)
            .httpOnly(true) // 클라이언트의 js에서 접근 불가
            .secure(true)
            .path("/") // 유효 경로 설정
            .sameSite("Strict") // 다른 도메인에 요청 시 쿠키 포함 불가
            .maxAge(60 * 30)  // 30분
            .build();

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh_token", refreshToken)
            .httpOnly(true) // 클라이언트의 js에서 접근 불가
            .secure(true)
            .path("/") // 유효 경로 설정
            .sameSite("Strict") // 다른 도메인에 요청 시 쿠키 포함 불가
            .maxAge(60 * 60 * 24 * 7)  // 7일
            .build();

        ResponseCookie newUserCookie = ResponseCookie.from("new_user", String.valueOf(newUser))
            .httpOnly(false) // 클라이언트의 js에서 접근 가능
            .secure(true)
            .path("/") // 유효 경로 설정
            .sameSite("Strict") // 다른 도메인에 요청 시 쿠키 포함 불가
            .maxAge(60 * 60 * 24)  // 1일
            .build();

        response.addHeader("Set-Cookie", accessTokenCookie.toString());
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());
        response.addHeader("Set-Cookie", newUserCookie.toString());
    }
}
