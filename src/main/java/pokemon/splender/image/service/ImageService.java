package pokemon.splender.image.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import pokemon.splender.exception.CustomMVCException;
import pokemon.splender.image.entity.Image;
import pokemon.splender.image.repository.ImageRepository;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final RedisTemplate<String, String> redisTemplate;

    public Map<Long, String> getImagesByIds(List<Long> ids) {
        // DB에서 이미지 경로 가져오기
        List<Image> images = imageRepository.findByIdIn(ids);

        if (images.isEmpty()) {
            throw CustomMVCException.notExistImage();
        }

        Map<Long, String> result = new HashMap<>();

        for (Image image : images) {

            if (image.getPath() == null || image.getPath().isBlank()) {
                throw CustomMVCException.invalidImage("이미지 경로가 비어있습니다. imageId : " + image.getId());
            }

            result.put(image.getId(),image.getPath());
        }
        return result;
    }

    public String getImageById(Long id) {
        Image image = imageRepository.findById(id)
            .orElseThrow(() -> CustomMVCException.notExistImage());

        if (image.getPath() == null || image.getPath().isBlank()) {
            throw CustomMVCException.invalidImage("이미지 경로가 비어있습니다. imageId : " + image.getId());
        }

        return image.getPath();
    }

    // Path별로 이미지를 찾은 후 Redis에 값 저장
    // 키 값 예시 : "image:legend:86" -> legend 폴더 안의 첫번째
    // 그룹으로 묶여있으므로 legend 그룹만 로딩 원할 시 "image:legend:*" 사용하면 된다.
    public void cacheImageGroup(String group, String pathPrefix) {
        List<Image> images = imageRepository.findAllByPathStartingWith(pathPrefix);

        if (images.isEmpty()) {
            throw CustomMVCException.notExistImage();
        }

        for (Image image : images) {

            if (image.getPath() == null || image.getPath().isBlank()) {
                throw CustomMVCException.invalidImage("이미지 경로가 비어있습니다. imageId : " + image.getId());
            }

            String redisKey = "image:" + group + ":" + image.getId();
            redisTemplate.opsForValue().set(redisKey, image.getPath());
        }
    }

}
