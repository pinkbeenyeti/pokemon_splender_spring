package pokemon.splender.image.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pokemon.splender.image.entity.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {

    Optional<List<Image>> findByIdIn(List<Long> ids);
}
