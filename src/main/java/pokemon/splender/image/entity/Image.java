package pokemon.splender.image.entity;


import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "images")
@NoArgsConstructor(access = PROTECTED)
public class Image {

    @Id
    @Getter
    private Long id;

    @Getter
    @Column(nullable = false)
    private String path;

}
