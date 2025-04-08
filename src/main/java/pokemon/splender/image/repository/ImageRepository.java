package pokemon.splender.image.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pokemon.splender.image.entity.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findByIdIn(List<Long> ids);
}
