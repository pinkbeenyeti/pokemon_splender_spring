package pokemon.splender.jwt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
// Lombok 어노테이션
// final이나 @NotNull이 붙은 필드를 자동으로 생성자에 포함시켜줌.
// 예를 들면 밑의 코드를 자동으로 만들어주는 것
//public RefreshTokenService(RedisTemplate<String, String> redisTemplate) {
//    this.redisTemplate = redisTemplate;
//}
// 왜 사용하는가?
// 의존성 주입을 위한 생성자 자동 생성할 때 유용하며,
// 필수 필드만 포함된 생성자만 만들어주기 때문에 깔끔하고 안전하다.
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String REFRESH_TOKEN_KEY = "refresh_token:";

    // Redis에 저장할 RefreshToken 키 값
    private String getRefreshTokenKey(Long userId) {
        return REFRESH_TOKEN_KEY + userId;
    }

    // Redis에 RefreshToken 저장하기
    // 키를 만든 후 값을 저장
    // Long expirationTime에 TTL 설정하면 됨
    public void saveRefreshToken(Long userId, String refreshToken, Long expirationTime) {
        String key = getRefreshTokenKey(userId);
        redisTemplate.opsForValue().set(key, refreshToken, expirationTime);
    }

    // Redis에서 RefreshToken 값 조회
    // opsForValue를 사용한 이유는 RedisTemplate가
    // 단순히 String으로 키-값으로 저장되어 있기 때문
    public String getRefreshToken(Long userId) {
        return redisTemplate.opsForValue().get(getRefreshTokenKey(userId));
    }

    // Redis에서 저장된 RefreshToken 키-값 삭제
    public void deleteRefreshToken(Long userId) {
        redisTemplate.delete(getRefreshTokenKey(userId));
    }

}
