package pokemon.splender.config;

import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pokemon.splender.image.service.ImageService;

@Component
@RequiredArgsConstructor
public class ImageCacheInitializer {

    private final ImageService imageService;

    @Value("${image.base-path}")
    private String imageBasePath;

    // 프로그램 시작 시 캐싱
    @PostConstruct
    public void init() {
        List<String> groups = List.of(
            "level1", "level2", "level3", "back", "legend", "rare", "token"
        );

        for (String groupName : groups) {
            String pathPrefix = imageBasePath + groupName + "/";
            imageService.cacheImageGroup(groupName, pathPrefix);
        }

    }

}
