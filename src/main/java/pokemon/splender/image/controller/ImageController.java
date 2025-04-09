package pokemon.splender.image.controller;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pokemon.splender.image.service.ImageService;
import pokemon.splender.response.CustomResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImageController {

    private final ImageService imageService;

    @PostMapping
    public ResponseEntity<Map<Long, String>> getImagesByIds(
        @RequestBody List<Long> ids
    ) {
        Map<Long, String> imageMap = imageService.getImagesByIds(ids);
        return CustomResponse.ok(imageMap);
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<String> getProfileImage(@PathVariable Long userId) {
        String image = imageService.getImageById(userId);
        return CustomResponse.ok(image);
    }

}
