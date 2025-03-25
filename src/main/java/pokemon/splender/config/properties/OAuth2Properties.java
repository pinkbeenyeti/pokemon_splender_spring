package pokemon.splender.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("oauth2")
@Getter
@Setter
public class OAuth2Properties {

    private String redirectDevUrl;
}
