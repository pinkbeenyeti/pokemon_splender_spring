package pokemon.splender.common.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.IOException;
import org.springframework.boot.test.context.TestConfiguration;
import redis.embedded.RedisServer;

@TestConfiguration
public class EmbeddedRedisConfig {

    private RedisServer redisServer;

    public EmbeddedRedisConfig() throws IOException {
        this.redisServer = new RedisServer(6379);
    }

    @PostConstruct
    public void start() {
        redisServer.start();
        System.out.println("Embedded Redis Server started on port 6379");
    }

    @PreDestroy
    public void stop() {
        redisServer.stop();
        System.out.println("Embedded Redis Server stopped");
    }

}
