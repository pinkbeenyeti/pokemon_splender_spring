package pokemon.splender.oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import pokemon.splender.config.properties.OAuth2Properties;
import pokemon.splender.exception.CustomFilterException;
import pokemon.splender.jwt.service.RefreshTokenService;
import pokemon.splender.jwt.util.JwtUtil;
import pokemon.splender.jwt.util.TokenCookieUtil;
import pokemon.splender.user.entity.User;
import pokemon.splender.user.service.UserService;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final OAuth2Properties oAuth2Properties;
    private final RefreshTokenService refreshTokenService;

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

        // Redis에 Refresh Token 저장
        long refreshTokenExpiration = jwtUtil.getRefreshTokenExpiration(); // Refresh Token 만료 시간
        refreshTokenService.saveRefreshToken(user.getId(), refreshToken, refreshTokenExpiration); // Redis에 저장

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
        response.addHeader("Set-Cookie", TokenCookieUtil.createAccessTokenCookie(accessToken).toString());
        response.addHeader("Set-Cookie", TokenCookieUtil.createRefreshTokenCookie(refreshToken).toString());
        response.addHeader("Set-Cookie", TokenCookieUtil.newUserCookie(newUser).toString());
    }
}
