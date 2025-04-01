package pokemon.splender.jwt.util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pokemon.splender.config.properties.JwtProperties;
import pokemon.splender.exception.CustomFilterException;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtProperties jwtProperties;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes());
    }

    public String createAccessToken(Long userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 3600000); // 만료 시간을 1시간으로 설정

        return Jwts.builder()
            .setSubject(String.valueOf(userId))
            .claim("tokenType", "access")
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(SignatureAlgorithm.HS256, getSigningKey()) // 명확한 서명 알고리즘과 키 사용
            .compact();
    }

    public String createRefreshToken(Long userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 604800000); // 만료 시간을 7일로 설정

        return Jwts.builder()
            .setSubject(String.valueOf(userId))
            .claim("tokenType", "refresh")
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(SignatureAlgorithm.HS256, getSigningKey()) // 명확한 서명 알고리즘과 키 사용
            .compact();
    }

    public String extractAccessToken(HttpServletRequest request) {
        if (request.getCookies() == null) {
            throw CustomFilterException.emptyCookiesException();
        }

        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("access_token")) {
                return cookie.getValue();
            }
        }

        throw CustomFilterException.notExistAccessTokenCookie();
    }

    public String extractRefreshToken(HttpServletRequest request) {
        if (request.getCookies() == null) {
            throw CustomFilterException.emptyCookiesException();
        }

        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("refresh_token")) {
                return cookie.getValue();
            }
        }

        throw CustomFilterException.notExistAccessTokenCookie();
    }

    public boolean validateToken(String token) {
        try {
            JwtParser jwtParser = Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) // 서명 키 설정
                .build();

            jwtParser.parseClaimsJws(token); // 토큰을 파싱하여 검증
            return true; // 검증 성공 토큰 만료 시 ExpiredJwtException 발생
        } catch (ExpiredJwtException e) {
            throw CustomFilterException.expiredTokenException();
        } catch (Exception e) {
            return false;
        }
    }

    public String getSubject(String token) {
        return Jwts.parser()
            .setSigningKey(getSigningKey())
            .parseClaimsJws(token)  // Jws 토큰 파싱
            .getBody()  // Claims를 가져옵니다.
            .getSubject();  // Subject 반환
    }
}
