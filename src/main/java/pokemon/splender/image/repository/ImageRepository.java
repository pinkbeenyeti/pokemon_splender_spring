package pokemon.splender.image.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pokemon.splender.image.entity.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findByIdIn(List<Long> ids);

    // Path(폴더)별로 이미지 찾기
    // 다음과 같은 쿼리 의미를 가짐
    // SELECT i FROM Image i WHERE i.path LIKE CONCAT(:basePath, '%')
    List<Image> findAllByPathStartingWith(String basePath);
}
