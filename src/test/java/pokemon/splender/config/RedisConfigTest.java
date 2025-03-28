package pokemon.splender.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@SpringBootTest
// Embedded Redis 적용
@Import(EmbeddedRedisConfig.class)
class RedisConfigTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    void testRedisConnect() {
        // Given
        String key = "testKey";
        String value = "testValue";

        // When
        // opsForValue : Strings를 쉽게 Serialize/Deserialize 해주는 Interface
        redisTemplate.opsForValue().set(key, value);
        String redisValue = redisTemplate.opsForValue().get(key);

        // Then
        assertThat(redisValue).isEqualTo(value);
    }

    @Test
    void testRedisStringStorage() {
        // Given
        // ValueOperation : RedisTemplate에서 단순한 Key-Value 저장하기 위한 인터페이스
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String key = "testKey";
        String value = "test Value is String Type";
        ops.set(key, value);

        // When
        String storedValue = ops.get(key);

        // Then
        assertThat(storedValue).isInstanceOf(String.class);
        assertThat(storedValue).isEqualTo(value);
        System.out.println("Redis 저장 값 확인 : " + storedValue);
    }

}