package pokemon.splender.user.entity;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pokemon.splender.exception.CustomMVCException;
import pokemon.splender.user.util.NameValidator;

@Entity
@Table(name = "users", uniqueConstraints = {
    @UniqueConstraint(name = "unique_user_provider_id", columnNames = {"providerId", "provider"})
})
@NoArgsConstructor(access = PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter
    @Column(nullable = false)
    private String name;

    @Getter
    @Column(nullable = false)
    private String providerId;

    @Getter
    @Column(nullable = false)
    private String provider;

    @Getter
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Getter
    @Column(nullable = false)
    private LocalDateTime lastNameChangedAt;

    public User(String providerId, String provider) {
        LocalDateTime now = LocalDateTime.now();

        this.name = createRandomName();
        this.providerId = providerId;
        this.provider = provider;
        this.createdAt = now;
        this.lastNameChangedAt = now.minusMonths(2);
    }

    public void updateName(String name) {
        if (!canChangeName()) {
            throw CustomMVCException.invalidNameChangePeriod();
        }

        NameValidator.validate(name);

        this.name = name;
        this.lastNameChangedAt = LocalDateTime.now();
    }

    private boolean canChangeName() {
        return lastNameChangedAt.isBefore(LocalDateTime.now().minusDays(30));
    }

    private String createRandomName() {
        return "User_" + UUID.randomUUID().toString().substring(0, 8);
    }
}
