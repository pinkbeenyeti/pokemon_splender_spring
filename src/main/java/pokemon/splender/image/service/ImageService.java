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
import org.springframework.stereotype.Service;
import pokemon.splender.exception.CustomMVCException;
import pokemon.splender.image.entity.Image;
import pokemon.splender.image.repository.ImageRepository;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

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
                throw CustomMVCException.invalidImage();
            }
        }
        return result;
    }

}
