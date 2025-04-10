package pokemon.splender.user.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pokemon.splender.exception.CustomMVCException;
import pokemon.splender.user.entity.User;
import pokemon.splender.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User createUser(String providerId, String provider) {
        User user = new User(providerId, provider);

        return userRepository.save(user);
    }

    public Optional<User> findByProviderIdAndProvider(String providerId, String provider) {
        return userRepository.findByProviderIdAndProvider(providerId, provider);
    }

    @Transactional
    public void updateUserName(Long userId, String newUserName) {
        User user = userRepository.findById(userId)
            .orElseThrow(CustomMVCException::userNotFound);

        if (isDuplicatedName(newUserName)) {
            throw CustomMVCException.duplicatedName();
        }

        user.updateName(newUserName);
    }

    private boolean isDuplicatedName(String name) {
        return userRepository.findByName(name).isPresent();
    }

}
