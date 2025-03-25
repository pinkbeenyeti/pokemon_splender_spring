package pokemon.splender;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import pokemon.splender.config.properties.JwtProperties;
import pokemon.splender.config.properties.OAuth2Properties;

@SpringBootApplication
@EnableConfigurationProperties({JwtProperties.class, OAuth2Properties.class})
public class SplenderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SplenderApplication.class, args);
    }

}
