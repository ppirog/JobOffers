package joboffers.domain.loginandregister;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface UserRepository extends MongoRepository<User, String> {

    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    default void updateIsAdminByUsername(String username, boolean isAdmin) {
        Optional<User> optionalUser = findByUsername(username);
        optionalUser.ifPresent(
                user -> {
                    User a = User.builder()
                            .username(user.getUsername())
                            .password(user.getPassword())
                            .isAdmin(isAdmin)
                            .build();
                    delete(user);
                    save(a);
                });
    }

}
