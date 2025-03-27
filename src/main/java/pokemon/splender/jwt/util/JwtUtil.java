package pokemon.splender.jwt.util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
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
            .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey()) // 명확한 서명 알고리즘과 키 사용
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
            .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey()) // 명확한 서명 알고리즘과 키 사용
            .compact();
    }

    public String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            throw CustomFilterException.invalidHeaderException();
        }

        return header.substring(7);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(getSigningKey()).build()
                .parseClaimsJws(token); // 해당 코드에서 jwt 만료 시 ExpiredJwtException 발생
            return true;
        } catch (ExpiredJwtException e) {
            throw CustomFilterException.expiredTokenException();
        } catch (Exception e) {
            return false;
        }
    }

    public String getSubject(String token) {
        return Jwts.parser()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }
}
