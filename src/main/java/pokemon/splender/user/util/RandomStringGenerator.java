package pokemon.splender.user.util;

import java.security.SecureRandom;
import java.util.UUID;

public class RandomStringGenerator {

    private static final String USER_PREFIX = "User_";

    public static String generateRandomUserName() {
        return USER_PREFIX + UUID.randomUUID().toString().replaceAll("-", "")
            .substring(0, 6).toUpperCase(); // base16 → 대문자
    }

    public static String generateRandomRoomId() {
        return UUID.randomUUID().toString().replaceAll("-", "")
            .substring(0, 8).toUpperCase(); // base16 → 대문자
    }

    public static String generateRandomInviteCode() {
        SecureRandom random = new SecureRandom();
        return String.format("%06d", random.nextInt(1_000_000)); // 가독성을 위한 100만에 언더바 사용
    }
}
