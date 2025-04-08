package pokemon.splender.image.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
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
            try {
                // 이미지 경로로부터 Path 객체 생성
                Path path = Paths.get(image.getPath());

                // 이미지 파일을 바이트 배열로 읽음
                byte[] bytes = Files.readAllBytes(path);

                // 이미지를 Base64 문자열로 인코딩
                String base64Image = Base64.getEncoder().encodeToString(bytes);

                result.put(image.getId(), base64Image);
            } catch (IOException e) {
                throw CustomMVCException.invalidImage(e.getMessage());
            }
        }
        return result;
    }

    public String getImagebyId(Long id) {
        Image image = imageRepository.findById(id)
            .orElseThrow(() -> CustomMVCException.notExistImage());

        try {
            Path path = Paths.get(image.getPath());
            byte[] bytes = Files.readAllBytes(path);
            String base64Image = Base64.getEncoder().encodeToString(bytes);
            return base64Image;
        } catch (IOException e) {
            throw CustomMVCException.invalidImage(e.getMessage());
        }
    }

    // Path별로 이미지를 찾은 후 Redis에 값 저장
    // 키 값 예시 : "image:legend:86" -> legend 폴더 안의 첫번째
    // 그룹으로 묶여있으므로 legend 그룹만 로딩 원할 시 "image:legend:*" 사용하면 된다.
    public void cacheImageGroup(String group, String pathPrefix) {
        List<Image> images = imageRepository.findAllByPathStartingWith(pathPrefix);

        for (Image image : images) {
            try {
                Path path = Paths.get(image.getPath());
                byte[] bytes = Files.readAllBytes(path);
                String base64Image = Base64.getEncoder().encodeToString(bytes);

                String redisKey = "image:" + group + ":" + image.getId();
                redisTemplate.opsForValue().set(redisKey, base64Image);
            } catch (IOException e) {
                throw CustomMVCException.invalidImage(e.getMessage());
            }
        }
    }

}
